package com.onlineorder.paymentservice.service;

import com.onlineorder.paymentservice.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse getPaymentById(Long id);
    List<PaymentResponse> getAllPayments();
}
