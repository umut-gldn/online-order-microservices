package com.onlineorder.restaurantservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateMenuItemRequest(
        @Size(min = 2, max = 160) String name,
        @Size(max = 500) String description,
        @DecimalMin("0.0") BigDecimal price,
        Boolean available
) {}

