package com.onlineorder.paymentservice.service.impl;

import com.onlineorder.paymentservice.dto.PaymentResponse;
import com.onlineorder.paymentservice.mapper.PaymentMapper;
import com.onlineorder.paymentservice.repository.PaymentRepository;
import com.onlineorder.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    @Override
    public PaymentResponse getPaymentById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(()->new RuntimeException("Payment not found with id: "+id));

    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }
}
