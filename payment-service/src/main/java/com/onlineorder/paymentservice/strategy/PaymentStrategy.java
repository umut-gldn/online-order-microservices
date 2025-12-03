package com.onlineorder.paymentservice.strategy;

import java.math.BigDecimal;

public interface PaymentStrategy {
    boolean processPayment(BigDecimal amount,String currency,String paymentToken);
}
