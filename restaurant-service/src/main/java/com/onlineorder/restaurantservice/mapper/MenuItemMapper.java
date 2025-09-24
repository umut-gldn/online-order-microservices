package com.onlineorder.restaurantservice.mapper;

import com.onlineorder.restaurantservice.dto.CreateMenuItemRequest;
import com.onlineorder.restaurantservice.dto.MenuItemResponse;
import com.onlineorder.restaurantservice.dto.UpdateMenuItemRequest;
import com.onlineorder.restaurantservice.model.MenuItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    MenuItem toEntity(CreateMenuItemRequest request);
    MenuItemResponse toResponse(MenuItem entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UpdateMenuItemRequest request, @MappingTarget MenuItem target);
}
