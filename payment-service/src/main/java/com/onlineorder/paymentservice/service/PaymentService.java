package com.onlineorder.paymentservice.service;

import com.onlineorder.paymentservice.dto.PaymentRequest;
import com.onlineorder.paymentservice.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    List<PaymentResponse> getAllPayments();
    PaymentResponse updatePayment(Long id, PaymentRequest request);
    void deletePayment(Long id);
}
