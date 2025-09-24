package com.onlineorder.orderservice.service;

import com.onlineorder.orderservice.dto.OrderRequest;
import com.onlineorder.orderservice.dto.OrderResponse;
import com.onlineorder.orderservice.model.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrder(Long id, OrderRequest request);
    void deleteOrder(Long id);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus status);
    void cancelOrder(Long orderId, String reason);
}
