package com.onlineorder.restaurantservice.dto;

public record RestaurantResponse(
        Long id, String name, String address, String phone
) {}
