package com.onlineorder.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service",path = "/api/restaurants")
public interface RestaurantClient {
    @GetMapping("/{restaurantId}/menu-items/{id}")
    RemoteMenuItem getMenuItem(@PathVariable("restaurantId") Long restaurantId, @PathVariable("id") Long id);

}
