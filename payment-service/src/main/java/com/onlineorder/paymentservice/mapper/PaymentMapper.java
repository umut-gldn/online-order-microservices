package com.onlineorder.paymentservice.mapper;

import com.onlineorder.paymentservice.dto.PaymentRequest;
import com.onlineorder.paymentservice.dto.PaymentResponse;
import com.onlineorder.paymentservice.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toEntity(PaymentRequest request);

    PaymentResponse toResponse(Payment payment);
    void updateFromRequest(PaymentRequest request,@MappingTarget Payment payment);
}
