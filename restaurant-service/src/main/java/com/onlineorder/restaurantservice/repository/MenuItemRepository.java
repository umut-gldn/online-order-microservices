package com.onlineorder.restaurantservice.repository;

import com.onlineorder.restaurantservice.model.MenuItem;
import com.onlineorder.restaurantservice.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Page<MenuItem> findAllByRestaurant(Restaurant restaurant, Pageable pageable);
}
