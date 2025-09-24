package com.onlineorder.courierservice.mapper;

import com.onlineorder.courierservice.dto.DeliveryResponse;
import com.onlineorder.courierservice.dto.UpdateDeliveryRequest;
import com.onlineorder.courierservice.model.Delivery;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryResponse toResponse(Delivery entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UpdateDeliveryRequest request, @MappingTarget Delivery target);
}
