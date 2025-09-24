package com.onlineorder.orderservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long restaurantId;

    private String deliveryAddress;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> orderItems=new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if(status==null){
            status=OrderStatus.PENDING;
        }
        if(totalPrice==null && !orderItems.isEmpty()) {
            calculateTotalPrice();
        }
    }

    @PreUpdate
    void preUpdate(){
        updatedAt = LocalDateTime.now();
        if (!orderItems.isEmpty()){
            calculateTotalPrice();
        }
    }
    public void calculateTotalPrice(){
        if(orderItems!= null && !orderItems.isEmpty()){
            this.totalPrice=orderItems.stream()
                    .map(item-> {
                        if(item.getUnitPrice()!=null && item.getQuantity()!=null){
                            return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        }
                        return BigDecimal.ZERO;
                    })
                    .reduce(BigDecimal.ZERO,BigDecimal::add);
        }
      else {
          this.totalPrice=BigDecimal.ZERO;
        }
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        calculateTotalPrice();
    }
}
