package com.example.Pet.clinic.repository;

import com.example.Pet.clinic.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findByStatus(String status);
    Optional<Clinic> findByUserId(Long userId);
}