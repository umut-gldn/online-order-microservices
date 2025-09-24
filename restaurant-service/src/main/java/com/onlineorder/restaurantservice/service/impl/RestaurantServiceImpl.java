package com.onlineorder.restaurantservice.service.impl;

import com.onlineorder.restaurantservice.dto.CreateRestaurantRequest;
import com.onlineorder.restaurantservice.dto.RestaurantResponse;
import com.onlineorder.restaurantservice.dto.UpdateMenuItemRequest;
import com.onlineorder.restaurantservice.dto.UpdateRestaurantRequest;
import com.onlineorder.restaurantservice.exception.NotFoundException;
import com.onlineorder.restaurantservice.mapper.RestaurantMapper;
import com.onlineorder.restaurantservice.model.Restaurant;
import com.onlineorder.restaurantservice.repository.RestaurantRepository;
import com.onlineorder.restaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;

    @Override
    public RestaurantResponse create(CreateRestaurantRequest request) {
        Restaurant saved=repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    public RestaurantResponse getById(Long id) {
        Restaurant restaurant=repository.findById(id).orElseThrow(()-> new NotFoundException("Restaurant not found: "+id));
        return mapper.toResponse(restaurant);
    }

    @Override
    public Page<RestaurantResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public RestaurantResponse update(Long id, UpdateRestaurantRequest request) {
        Restaurant restaurant=repository.findById(id).orElseThrow(()->new NotFoundException("Restaurant not found: "+id));
        mapper.updateFromRequest(request,restaurant);
        return mapper.toResponse(repository.save(restaurant));
    }

    @Override
    public void delete(Long id) {
        if(!repository.existsById(id)){
            throw new NotFoundException("Restaurant not found: "+id);
        }
        repository.deleteById(id);

    }
}
