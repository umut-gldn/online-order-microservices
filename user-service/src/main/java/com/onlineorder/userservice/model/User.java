package com.onlineorder.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users",
indexes = {@Index (name="idx_users_email",columnList = "email", unique = true) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true,length = 120)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,length = 120)
    private String fullName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //first save, then update
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
