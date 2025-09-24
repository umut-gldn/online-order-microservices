package com.onlineorder.courierservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "deliveries")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 40)
    private DeliveryStatus status;

    @Column(length = 255)
    private String pickupAddress;

    @Column(length = 255)
    private String dropoffAddress;

    private Integer estimatedMinutes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status==null){
            status=DeliveryStatus.ASSIGNED;
        }
    }
    @PreUpdate
    private void preUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
