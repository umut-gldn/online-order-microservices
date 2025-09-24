package com.onlineorder.courierservice.dto;

import com.onlineorder.courierservice.model.DeliveryStatus;
import jakarta.validation.constraints.Size;

public record UpdateDeliveryRequest(
        Long courierId,
        DeliveryStatus status,
        @Size(max = 255) String pickupAddress,
        @Size(max = 255) String dropoffAddress,
        Integer estimatedMinutes
) {}
