package com.onlineorder.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Restaurant Service'den gelen menu item bilgileri
    @Column(nullable = false)
    private Long menuItemId;

    @Column(nullable = false)
    private String menuItemName;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    // Hesaplanmış toplam fiyat
    @Column(nullable = false)
    private BigDecimal totalItemPrice;

    @PrePersist
    @PreUpdate
    void calculateTotalItemPrice() {
        if (unitPrice != null && quantity != null) {
            this.totalItemPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
