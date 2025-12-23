package com.example.demo.repository;

import com.example.demo.entity.VerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationLogRepository
        extends JpaRepository<VerificationLog, Long> {

    // ✅ REQUIRED by VerificationServiceImpl
    List<VerificationLog> findByCertificateId(Long certificateId);
}
