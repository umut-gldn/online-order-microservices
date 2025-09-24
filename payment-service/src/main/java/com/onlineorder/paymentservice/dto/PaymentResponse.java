package com.onlineorder.paymentservice.dto;

import com.onlineorder.paymentservice.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
         Long id,
         Long orderId,
         Long userId,
         BigDecimal amount,
         PaymentStatus status,
         LocalDateTime createdAt,
         LocalDateTime updatedAt
) {}
