package com.onlineorder.courierservice.dto;

import com.onlineorder.courierservice.model.DeliveryStatus;

public record DeliveryResponse(
        Long id,
        Long orderId,
        Long courierId,
        DeliveryStatus status,
        String pickupAddress,
        String dropoffAddress,
        Integer estimatedMinutes
) {
}
