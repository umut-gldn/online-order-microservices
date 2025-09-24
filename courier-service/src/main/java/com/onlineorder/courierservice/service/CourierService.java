package com.onlineorder.courierservice.service;

import com.onlineorder.courierservice.dto.CourierResponse;
import com.onlineorder.courierservice.dto.CreateCourierRequest;
import com.onlineorder.courierservice.dto.UpdateCourierRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourierService {
    CourierResponse create(CreateCourierRequest request);
    CourierResponse getById(Long id);
    Page<CourierResponse> list(Pageable pageable);
    CourierResponse update(Long id, UpdateCourierRequest request);
    void delete(Long id);
}
