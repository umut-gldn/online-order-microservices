package com.onlineorder.orderservice.service.impl;

import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.NotificationEvent;
import com.onlineorder.common.events.OrderEvent;
import com.onlineorder.common.events.PaymentEvent;
import com.onlineorder.common.messaging.MessagePublisher;
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
import com.onlineorder.orderservice.service.OrderService;
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
    private final MessagePublisher messagePublisher;
    private final UserClient userClient;
    private final RestaurantClient restaurantClient;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        var user = userClient.getById(request.userId());
        if(user==null){
            throw new RuntimeException("User not found with id: "+request.userId());
        }
        log.info("Creating order: {}",request.userId());

        Order order=mapper.toEntity(request);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderItems(new ArrayList<>());

        for(OrderRequest.OrderItemRequest itemRequest :request.orderItems()){
            var remote=restaurantClient.getMenuItem(request.restaurantId(),itemRequest.menuItemId());
            if(remote==null || remote.available().equals(false)){
                throw  new RuntimeException("Menu item not available "+itemRequest.menuItemId());
            }
            OrderItem orderItem=OrderItem.builder()
                    .menuItemId(remote.id())
                    .menuItemName(remote.name())
                    .unitPrice(remote.price())
                    .quantity(itemRequest.quantity())
                    .build();
            orderItem.setTotalItemPrice(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            order.addOrderItem(orderItem);
        }
        Order savedOrder=repository.save(order);
        OrderResponse response=mapper.toResponse(savedOrder);

        publishOrderCreatedEvent(savedOrder);
        publishOrderNotificationEvent(savedOrder,"ORDER_CREATED");
        publishPaymentProcessEvent(savedOrder);
        log.info("Order created succesfully with ID: {}",savedOrder.getId());
        return response;
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(()->new RuntimeException("Order not found with id: "+id));
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
        log.info("Updating order: {}",id);

        var user=userClient.getById(request.userId());
        if(user==null){
            throw new RuntimeException("User not found with id: "+request.userId());
        }
        Order order=repository.findById(id).orElseThrow(()->new RuntimeException("Order not found with id: "+id));
        OrderStatus previousStatus=order.getStatus();

        mapper.updateFromRequest(request,order);
        order.getOrderItems().clear();
        for(OrderRequest.OrderItemRequest itemRequest :request.orderItems()){
            var remote=restaurantClient.getMenuItem(request.restaurantId(),itemRequest.menuItemId());
            if(remote==null || remote.available().equals(false)){
                throw  new RuntimeException("Menu item  unavailable "+itemRequest.menuItemId());
            }
            OrderItem orderItem=OrderItem.builder()
                    .menuItemId(remote.id())
                    .menuItemName(remote.name())
                    .unitPrice(remote.price())
                    .quantity(itemRequest.quantity())
                    .build();
            orderItem.setTotalItemPrice(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            order.addOrderItem(orderItem);
        }

        Order updatedOrder=repository.save(order);

        if(!previousStatus.equals(updatedOrder.getStatus())){
            publishOrderUpdatedEvent(updatedOrder,previousStatus);
            publishOrderNotificationEvent(updatedOrder,"ORDER_STATUS_CHANGED");
        }
        log.info("Order {} updated succesfully",id);
        return mapper.toResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if(!repository.existsById(id)){
            throw new RuntimeException("Order not found with id: "+id);
        }
        Order order=repository.findById(id).get();
        repository.deleteById(id);
        publishOrderCancelledEvent(order,"Order cancelled by user");
        publishOrderNotificationEvent(order,"ORDER_CANCELLED");
    }

    @Transactional
    public  OrderResponse updateOrderStatus(Long orderId,OrderStatus status){
        log.info("Updating order status: {} to {}",orderId,status);
        Order order=repository.findById(orderId).orElseThrow(()->new RuntimeException("Order not found with id: "+orderId));

        if(order.getStatus()==status){
            log.info("Order {} already in status {}, skipping.",orderId,status);
            return mapper.toResponse(order);
        }

        OrderStatus previousStatus=order.getStatus();
        order.setStatus(status);
        Order updatedOrder=repository.save(order);

        publishOrderUpdatedEvent(updatedOrder,previousStatus);
        publishOrderNotificationEvent(updatedOrder,"ORDER_STATUS_CHANGED");

        log.info("Order {} status updated to {}",orderId,status);
        return mapper.toResponse(updatedOrder);
    }
    @Override
    @Transactional
    public void cancelOrder(Long orderId,String reason){
        log.info("Cancelling order: {} with reason: {}",orderId,reason);
        Order order=repository.findById(orderId).orElseThrow(()->new RuntimeException("Order not found with id: "+orderId));

        if(order.getStatus()==OrderStatus.CANCELLED){
            log.info("Order {} already cancelled, skipping.",orderId);
            return;
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder=repository.save(order);

        publishOrderCancelledEvent(cancelledOrder,reason);
        publishOrderNotificationEvent(cancelledOrder,"ORDER_CANCELLED");
        log.info("Order {} cancelled with reason: {}",orderId,reason);
    }


    private void publishOrderCreatedEvent(Order order){
        OrderEvent event=convertToOrderEvent(order);
        messagePublisher.publishMessage(RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_CREATED_KEY,event);
    }
    private void publishOrderUpdatedEvent(Order order,OrderStatus previousStatus){
        OrderEvent event=convertToOrderEvent(order);
        messagePublisher.publishMessage(RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_UPDATED_KEY,event);
    }
    private void publishOrderCancelledEvent(Order order,String reason){
        OrderEvent event=convertToOrderEvent(order);
        messagePublisher.publishMessage(RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_CANCELLED_KEY,event);
    }
    private void publishOrderNotificationEvent(Order order,String eventType ){

        NotificationEvent notificationEvent=new NotificationEvent();
        notificationEvent.setUserId(order.getUserId());
        notificationEvent.setType(NotificationEvent.NotificationType.EMAIL);
        notificationEvent.setCreatedAt(LocalDateTime.now());

        try{
            var u=userClient.getById(order.getUserId());
            if(u !=null){
                notificationEvent.setEmail(u.email());
            }
        }
        catch (Exception ignored){}

        Map<String, Object> templateData=new HashMap<>();
        templateData.put("orderId",order.getId());
        templateData.put("status",order.getStatus());
        templateData.put("totalPrice",order.getTotalPrice());
        templateData.put("adress",order.getDeliveryAddress());

        switch (eventType){
            case "ORDER_CREATED":
                notificationEvent.setSubject("Order confirmation - #"+order.getId());
                notificationEvent.setMessage("Your order has been confirmed. Thank you for your order.");
                break;
            case "ORDER_STATUS_CHANGED":
                notificationEvent.setSubject("Order status changed - #"+order.getId());
                notificationEvent.setMessage("Your order status has changed to "+order.getStatus());
                break;
            case "ORDER_CANCELLED":
                notificationEvent.setSubject("Order CANCELLED - #"+order.getId());
                notificationEvent.setMessage("Your order has been cancelled");
                break;
        }
        notificationEvent.setTemplateData(templateData);
        messagePublisher.publishMessage(RabbitMQConfig.NOTIFICATION_EXCHANGE
        ,RabbitMQConfig.NOTIFICATION_EMAIL_KEY,notificationEvent);
    }

    private void publishPaymentProcessEvent(Order order){
        PaymentEvent event=new PaymentEvent();
        event.setOrderId(order.getId());
        event.setUserId(order.getUserId());
        event.setAmount(order.getTotalPrice());
        event.setStatus(PaymentEvent.PaymentStatus.PENDING);
        event.setProcessedAt(LocalDateTime.now());
        messagePublisher.publishMessage(RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_PROCESS_KEY,event);

    }

    private OrderEvent convertToOrderEvent(Order order) {
        OrderEvent event = new OrderEvent();
        event.setOrderId(order.getId());
        event.setUserId(order.getUserId());
        event.setRestaurantId(order.getRestaurantId());
        event.setStatus(mapOrderStatus(order.getStatus()));
        event.setTotalAmount(order.getTotalPrice());
        event.setDeliveryAddress(order.getDeliveryAddress());
        event.setCreatedAt(order.getCreatedAt());
        event.setUpdatedAt(order.getUpdatedAt());
        return event;
    }
    private OrderEvent.OrderStatus mapOrderStatus(OrderStatus status) {
        return switch (status) {
            case PENDING -> OrderEvent.OrderStatus.PENDING;
            case CONFIRMED -> OrderEvent.OrderStatus.CONFIRMED;
            case CANCELLED -> OrderEvent.OrderStatus.CANCELLED;
            case DELIVERED -> OrderEvent.OrderStatus.DELIVERED;
        };
    }

}
