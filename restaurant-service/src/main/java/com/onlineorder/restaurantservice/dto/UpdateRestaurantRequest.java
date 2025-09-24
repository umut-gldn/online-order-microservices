package com.onlineorder.restaurantservice.dto;

import jakarta.validation.constraints.Size;

public record UpdateRestaurantRequest(
        @Size(min = 2, max = 160) String name,
        @Size(max = 255) String address,
        @Size(max = 40) String phone
) {}
