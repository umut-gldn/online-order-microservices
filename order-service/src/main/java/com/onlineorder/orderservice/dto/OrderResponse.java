package com.onlineorder.orderservice.dto;

import com.onlineorder.orderservice.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
         Long id,
         Long userId,
         Long restaurantId,
         String deliveryAddress,
         BigDecimal totalPrice,
         OrderStatus status,
         List<OrderItemResponse> orderItems,
         LocalDateTime createdAt,
         LocalDateTime updatedAt
) {
    public record OrderItemResponse(
            Long id,
            Long menuItemId,
            String menuItemName,
            BigDecimal unitPrice,
            Integer quantity,
            BigDecimal totalItemPrice
    ) {}
}
