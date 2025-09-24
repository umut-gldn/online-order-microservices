package com.onlineorder.courierservice.dto;

import com.onlineorder.courierservice.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDeliveryRequest(
        @NotNull Long orderId,
        @NotNull Long courierId,
        @NotNull DeliveryStatus status,
        @Size(max = 255) String pickupAddress,
        @Size(max = 255) String dropoffAddress,
        Integer estimatedMinutes
) {
}
