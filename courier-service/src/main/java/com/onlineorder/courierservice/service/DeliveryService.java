package com.onlineorder.courierservice.service;

import com.onlineorder.courierservice.dto.CreateDeliveryRequest;
import com.onlineorder.courierservice.dto.DeliveryResponse;
import com.onlineorder.courierservice.dto.UpdateDeliveryRequest;
import com.onlineorder.courierservice.model.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryService {
    DeliveryResponse assign(CreateDeliveryRequest request);
    DeliveryResponse getById(Long id);
    Page<DeliveryResponse> list(Pageable pageable);
    Page<DeliveryResponse> listByCourier(Long courierId, Pageable pageable);
    Page<DeliveryResponse> listByStatus(DeliveryStatus status, Pageable pageable);
    DeliveryResponse update(Long id, UpdateDeliveryRequest request);
    void delete(Long id);
}
