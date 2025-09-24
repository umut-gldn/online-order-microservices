package com.onlineorder.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
       @NotNull(message = "User ID is required")
       Long userId,
       @NotNull(message = "Restaurant ID is required")
       Long restaurantId,
       @NotBlank(message = "Delivery address is required")
       String deliveryAddress,
       @NotEmpty (message = "Order items cannot be empty ")
       @Valid
       List<OrderItemRequest> orderItems
) {
    public record OrderItemRequest(
            @NotNull(message = "Menu item ID is required")
            Long menuItemId,

            @NotNull(message = "Quantity is required")
            @Positive(message = "Quantity must be positive")
            Integer quantity
    ) {}
}
