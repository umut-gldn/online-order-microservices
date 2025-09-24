package com.onlineorder.restaurantservice.service.impl;

import com.onlineorder.restaurantservice.dto.CreateMenuItemRequest;
import com.onlineorder.restaurantservice.dto.MenuItemResponse;
import com.onlineorder.restaurantservice.dto.UpdateMenuItemRequest;
import com.onlineorder.restaurantservice.exception.NotFoundException;
import com.onlineorder.restaurantservice.mapper.MenuItemMapper;
import com.onlineorder.restaurantservice.model.MenuItem;
import com.onlineorder.restaurantservice.model.Restaurant;
import com.onlineorder.restaurantservice.repository.MenuItemRepository;
import com.onlineorder.restaurantservice.repository.RestaurantRepository;
import com.onlineorder.restaurantservice.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuRepository;
    private final MenuItemMapper mapper;
    private final RestaurantRepository restRepository;

    private Restaurant getRestaurantOrThrow(Long id){
        return restRepository.findById(id).orElseThrow(()->new NotFoundException("Restaurant not found: "+id));
    }

    private MenuItem getMenuItemOrThrow(Long id){
        return menuRepository.findById(id).orElseThrow(()->new NotFoundException("MenuItem not found: "+id));
    }


    @Override
    public MenuItemResponse create(Long restaurantId, CreateMenuItemRequest request) {
        Restaurant restaurant=getRestaurantOrThrow(restaurantId);
        MenuItem menuItem=mapper.toEntity(request);
        menuItem.setRestaurant(restaurant);
        return mapper.toResponse(menuRepository.save(menuItem));
    }

    @Override
    public MenuItemResponse getById(Long restaurantId, Long id) {
        Restaurant restaurant=getRestaurantOrThrow(restaurantId);
        MenuItem menuItem=getMenuItemOrThrow(id);
        if(!menuItem.getRestaurant().getId().equals(restaurantId)){
            throw new NotFoundException("MenuItem does not belong to restaurant ");
        }
        return mapper.toResponse(menuItem);
    }

    @Override
    public Page<MenuItemResponse> list(Long restaurantId, Pageable pageable) {
        Restaurant restaurant=getRestaurantOrThrow(restaurantId);
        return menuRepository.findAllByRestaurant(restaurant,pageable).map(mapper::toResponse);
    }

    @Override
    public MenuItemResponse update(Long restaurantId,Long id, UpdateMenuItemRequest request) {
        MenuItem menuItem=getMenuItemOrThrow(id);
        Restaurant restaurant=getRestaurantOrThrow(restaurantId);
        if(!menuItem.getRestaurant().getId().equals(restaurantId)){
            throw  new NotFoundException("MenuItem does not belong to restaurant ");
        }
        mapper.updateFromRequest(request,menuItem);
        return mapper.toResponse(menuRepository.save(menuItem));
    }

    @Override
    public void delete(Long restaurantId,Long id) {
        Restaurant restaurant=getRestaurantOrThrow(restaurantId);
        MenuItem menuItem=getMenuItemOrThrow(id);
        if(!menuItem.getRestaurant().getId().equals(restaurantId)){
            throw  new NotFoundException("MenuItem does not belong to restaurant ");
        }
        menuRepository.deleteById(id);
    }
}
