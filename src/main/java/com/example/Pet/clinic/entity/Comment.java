package com.example.Pet.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private LocalDateTime createdAt = LocalDateTime.now();
}

