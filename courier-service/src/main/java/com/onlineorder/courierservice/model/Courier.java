package com.onlineorder.courierservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "couriers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 160)
    private String fullName;

    @Column(length = 40)
    private String phone;

    @Column(length = 40)
    private String vehicleType;

    @Column(nullable = false)
    private Boolean active = true;

    private Double currentLat;
    private Double currentLng;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
   private void preUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
