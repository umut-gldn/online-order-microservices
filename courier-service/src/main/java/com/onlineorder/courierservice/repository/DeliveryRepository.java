package com.onlineorder.courierservice.repository;

import com.onlineorder.courierservice.model.Courier;
import com.onlineorder.courierservice.model.Delivery;
import com.onlineorder.courierservice.model.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Page<Delivery> findAllByCourier(Courier courier, Pageable pageable);
    Page<Delivery> findAllByStatus(DeliveryStatus status, Pageable pageable);
}
