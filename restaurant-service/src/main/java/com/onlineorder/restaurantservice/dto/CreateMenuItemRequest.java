package com.onlineorder.restaurantservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateMenuItemRequest(
        @NotBlank @Size(min = 2, max = 160) String name,
        @Size(max = 500) String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        Boolean available,
        String imageUrl
) {}
