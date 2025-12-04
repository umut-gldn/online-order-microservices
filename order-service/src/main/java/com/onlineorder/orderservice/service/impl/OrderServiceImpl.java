package com.onlineorder.orderservice.service.impl;

import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.NotificationEvent;
import com.onlineorder.common.events.OrderEvent;
import com.onlineorder.common.events.PaymentEvent;
import com.onlineorder.common.messaging.MessagePublisher;
import com.onlineorder.orderservice.client.RemoteMenuItem;
import com.onlineorder.orderservice.client.RemoteUser;
import com.onlineorder.orderservice.client.RestaurantClient;
import com.onlineorder.orderservice.client.UserClient;
import com.onlineorder.orderservice.dto.OrderRequest;
import com.onlineorder.orderservice.dto.OrderResponse;
import com.onlineorder.orderservice.mapper.OrderMapper;
import com.onlineorder.orderservice.model.Order;
import com.onlineorder.orderservice.model.OrderItem;
import com.onlineorder.orderservice.model.OrderStatus;
import com.onlineorder.orderservice.repository.OrderRepository;
import com.onlineorder.orderservice.service.OrderEventManager;
import com.onlineorder.orderservice.service.OrderService;
import com.onlineorder.orderservice.service.RestaurantIntegrationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final UserClient userClient;
    private final RestaurantIntegrationService restaurantIntegrationService;
    private final OrderEventManager eventManager;

    @Override
    @Transactional
    @CircuitBreaker(name="user-service",fallbackMethod = "fallbackUserCheck")
    public OrderResponse createOrder(OrderRequest request) {
        userClient.getById(request.userId());
        Order order=mapper.toEntity(request);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderItems(new ArrayList<>());

        processOrderItems(request,order);
        Order savedOrder=repository.save(order);
        eventManager.publishOrderCreated(savedOrder);

        log.info("Creating order for user: {}",request.userId());
        return mapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return mapper.toResponse(getOrderOrThrow(id));
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {

        userClient.getById(request.userId());

        Order order=getOrderOrThrow(id);
        OrderStatus previousStatus=order.getStatus();

        mapper.updateFromRequest(request,order);
        order.getOrderItems().clear();
        processOrderItems(request,order);

        Order updatedOrder=repository.save(order);

        if(!previousStatus.equals(updatedOrder.getStatus())){
            eventManager.publishOrderUpdated(updatedOrder,previousStatus);
        }
        log.info("Order {} updated succesfully",id);
        return mapper.toResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
       Order order=getOrderOrThrow(id);
       repository.deleteById(id);
       eventManager.publishOrderCancelled(order,"Order deleted by user");
    }

    @Transactional
    public  OrderResponse updateOrderStatus(Long orderId,OrderStatus status){
        Order order=getOrderOrThrow(orderId);

        if(order.getStatus()==status){
            return mapper.toResponse(order);
        }

        OrderStatus previousStatus=order.getStatus();
        order.setStatus(status);
        Order saved=repository.save(order);

       eventManager.publishOrderUpdated(saved,previousStatus);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId,String reason){
        Order order=getOrderOrThrow(orderId);

        if(order.getStatus()==OrderStatus.CANCELLED) return;

        order.setStatus(OrderStatus.CANCELLED);
        repository.save(order);

        eventManager.publishOrderCancelled(order,reason);
        log.info("Order {} cancelled with reason: {}",orderId,reason);
    }

    private void processOrderItems(OrderRequest request,Order order){
        for(OrderRequest.OrderItemRequest itemRequest:request.orderItems()){
            RemoteMenuItem remote=restaurantIntegrationService.getMenuItemSafely(request.restaurantId(), itemRequest.menuItemId());

            if(remote==null || Boolean.FALSE.equals(remote.available())){
                throw new RuntimeException("Menu item unavailable: "+itemRequest.menuItemId());
            }
            OrderItem orderItem=OrderItem.builder()
                    .menuItemId(remote.id())
                    .menuItemName(remote.name())
                    .unitPrice(remote.price())
                    .quantity(itemRequest.quantity())
                    .build();
            orderItem.setTotalItemPrice(remote.price().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            order.addOrderItem(orderItem);
        }
    }

    public OrderResponse fallbackUserCheck(Throwable t){
        log.error("User service is unavailable. {}",t.getMessage());
        throw new RuntimeException("User service is unavailable.");
    }

    private Order getOrderOrThrow(Long id){
        return repository.findById(id).orElseThrow(()->new RuntimeException("Order not found with id: "+id));
    }

}
