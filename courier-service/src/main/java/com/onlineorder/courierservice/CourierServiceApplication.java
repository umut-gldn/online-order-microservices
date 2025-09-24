package com.onlineorder.courierservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.onlineorder.courierservice","com.onlineorder.common"})
public class CourierServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourierServiceApplication.class, args);
    }
}
