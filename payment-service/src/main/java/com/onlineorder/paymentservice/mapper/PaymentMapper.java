package com.onlineorder.paymentservice.mapper;

import com.onlineorder.paymentservice.dto.PaymentResponse;
import com.onlineorder.paymentservice.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);

}
