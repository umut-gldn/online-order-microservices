package com.onlineorder.courierservice.repository;

import com.onlineorder.courierservice.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierRepository extends JpaRepository<Courier,Long> {
    List<Courier> findAllByActiveTrue();
}
