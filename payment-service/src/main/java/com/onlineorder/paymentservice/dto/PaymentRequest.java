package com.onlineorder.paymentservice.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull Long orderId,
        @NotNull Long userId,
        @NotNull BigDecimal amount
) {
}
