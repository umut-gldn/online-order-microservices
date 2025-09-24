package com.onlineorder.courierservice.dto;

import jakarta.validation.constraints.Size;

public record UpdateCourierRequest(
        @Size(min = 2, max = 160) String fullName,
        @Size(max = 40) String phone,
        @Size(max = 40) String vehicleType,
        Boolean active,
        Double currentLat,
        Double currentLng
) {
}
