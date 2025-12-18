package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.VerificationLog;

public interface VerificationLogRepository extends JpaRepository<VerificationLog, Long> {
}
