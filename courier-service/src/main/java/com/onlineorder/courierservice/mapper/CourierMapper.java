package com.onlineorder.courierservice.mapper;

import com.onlineorder.courierservice.dto.CourierResponse;
import com.onlineorder.courierservice.dto.CreateCourierRequest;
import com.onlineorder.courierservice.dto.UpdateCourierRequest;
import com.onlineorder.courierservice.model.Courier;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CourierMapper {
    Courier toEntity(CreateCourierRequest request);
    CourierResponse toResponse(Courier entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UpdateCourierRequest request, @MappingTarget Courier target);
}
