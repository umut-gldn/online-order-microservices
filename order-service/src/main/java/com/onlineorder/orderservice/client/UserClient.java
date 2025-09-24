package com.onlineorder.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",path = "/api/users")
public interface UserClient {
    @GetMapping("/{id}")
    RemoteUser getById(@PathVariable("id") Long id);
}
