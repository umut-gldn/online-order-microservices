package com.onlineorder.orderservice.client;

import java.math.BigDecimal;

public record RemoteMenuItem(Long id, String name,
                             String description, BigDecimal price,
                             Boolean available) {
}
