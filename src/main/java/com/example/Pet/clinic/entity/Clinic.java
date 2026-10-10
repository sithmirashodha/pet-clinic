package com.example.Pet.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;
    private String phone;

    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}