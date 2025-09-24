package com.onlineorder.restaurantservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRestaurantRequest(
        @NotBlank @Size(min = 2, max = 160) String name,
        @Size(max = 255) String address,
        @Size(max = 40) String phone
) {}
