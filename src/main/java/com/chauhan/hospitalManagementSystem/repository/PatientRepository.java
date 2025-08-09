package com.chauhan.hospitalManagementSystem.repository;

import com.chauhan.hospitalManagementSystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}