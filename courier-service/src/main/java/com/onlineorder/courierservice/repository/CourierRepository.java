package com.onlineorder.courierservice.repository;

import com.onlineorder.courierservice.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<Courier,Long> {
}
