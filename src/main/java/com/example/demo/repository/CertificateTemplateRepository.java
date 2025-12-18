package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.CertificateTemplate;

public interface CertificateTemplateRepository extends JpaRepository<CertificateTemplate, Long> {
}
