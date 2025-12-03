package com.onlineorder.paymentservice.strategy;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@ConditionalOnProperty(name="payment.provider",havingValue = "mock",matchIfMissing = true)
public class MockPaymentStrategy implements PaymentStrategy{


    @Override
    public boolean processPayment(BigDecimal amount, String currency, String paymentToken) {
        log.info("Mock payment process started for amount={}",amount);

        return amount != null && amount.compareTo(BigDecimal.valueOf(3000))<0;
    }
}
