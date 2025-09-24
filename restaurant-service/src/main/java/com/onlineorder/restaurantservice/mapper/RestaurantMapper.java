package com.onlineorder.restaurantservice.mapper;

import com.onlineorder.restaurantservice.dto.CreateRestaurantRequest;
import com.onlineorder.restaurantservice.dto.RestaurantResponse;
import com.onlineorder.restaurantservice.dto.UpdateRestaurantRequest;
import com.onlineorder.restaurantservice.model.Restaurant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant toEntity(CreateRestaurantRequest request);
    RestaurantResponse toResponse(Restaurant entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UpdateRestaurantRequest request,@MappingTarget Restaurant target);

}
