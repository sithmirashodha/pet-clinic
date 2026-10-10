package com.example.Pet.clinic.repository;

import com.example.Pet.clinic.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByQuestionId(Long questionId);
    void deleteByClinicId(Long clinicId);
}