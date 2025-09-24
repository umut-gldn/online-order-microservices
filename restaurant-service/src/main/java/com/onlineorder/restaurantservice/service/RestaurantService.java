package com.onlineorder.restaurantservice.service;

import com.onlineorder.restaurantservice.dto.CreateRestaurantRequest;
import com.onlineorder.restaurantservice.dto.RestaurantResponse;
import com.onlineorder.restaurantservice.dto.UpdateRestaurantRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {
    RestaurantResponse create(CreateRestaurantRequest request);
    RestaurantResponse getById(Long id);
    Page<RestaurantResponse> list(Pageable pageable);
    RestaurantResponse update(Long id, UpdateRestaurantRequest request);
    void delete(Long id);
}
