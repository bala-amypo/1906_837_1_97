package com.example.demo.repository;

import com.example.demo.entity.Certificate;
import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository
        extends JpaRepository<Certificate, Long> {

    Certificate findByVerificationCode(String code);
    Certificate findByStudent(Student student);
}
