package com.example.Pet.clinic.repository;

import com.example.Pet.clinic.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByOrderByCreatedAtDesc();
    List<Question> findByOwnerEmailOrderByCreatedAtDesc(String email);
}