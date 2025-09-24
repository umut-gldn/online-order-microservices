package com.onlineorder.courierservice.dto;

public record CourierResponse(
        Long id,
        String fullName,
        String phone,
        String vehicleType,
        Boolean active,
        Double currentLat,
        Double currentLng
) {
}
