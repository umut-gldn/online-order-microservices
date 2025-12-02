package com.onlineorder.orderservice.service;


import com.onlineorder.orderservice.client.RemoteMenuItem;
import com.onlineorder.orderservice.client.RestaurantClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantIntegrationService {
    private final RestaurantClient restaurantClient;

    @CircuitBreaker(name= "restaurant-service",fallbackMethod = "fallbackGetMenuItem")
    public RemoteMenuItem getMenuItemSafely(Long restaurantId, Long menuItemId){
        return restaurantClient.getMenuItem(restaurantId, menuItemId);
    }

    public RemoteMenuItem fallbackGetMenuItem(Long restaurantId, Long menuItemId,Throwable t){
        log.error("Restaurant Service unreachable for Item ID: {}. Error: {}", menuItemId, t.getMessage());
        // null dönüyoruz, çağıran yer (OrderService) bunu kontrol edip hata fırlatacak
        return null;
    }
}
