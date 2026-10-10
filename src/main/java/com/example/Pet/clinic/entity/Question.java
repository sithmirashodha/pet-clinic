package com.example.Pet.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String category; // VACCINE, DISEASE, FOOD, OTHER

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalDateTime createdAt = LocalDateTime.now();
}