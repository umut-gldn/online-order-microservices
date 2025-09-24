package com.onlineorder.restaurantservice.service;

import com.onlineorder.restaurantservice.dto.CreateMenuItemRequest;
import com.onlineorder.restaurantservice.dto.MenuItemResponse;
import com.onlineorder.restaurantservice.dto.UpdateMenuItemRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuItemService {
    MenuItemResponse create(Long restaurantId, CreateMenuItemRequest request);
    MenuItemResponse getById(Long restaurantId, Long id);
    Page<MenuItemResponse> list(Long restaurantId, Pageable pageable);
    MenuItemResponse update(Long restaurantId,Long id, UpdateMenuItemRequest request);
    void delete(Long restaurantId,Long id);
}
