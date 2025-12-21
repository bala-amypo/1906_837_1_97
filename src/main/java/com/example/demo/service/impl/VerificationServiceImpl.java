package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.VerificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VerificationServiceImpl implements VerificationService {
    private final VerificationLogRepository logRepository;
    private final CertificateRepository certificateRepository;

    public VerificationServiceImpl(VerificationLogRepository logRepository, 
                                   CertificateRepository certificateRepository) {
        this.logRepository = logRepository;
        this.certificateRepository = certificateRepository;
    }

    @Override
    public VerificationLog verifyCertificate(String code, String ipAddress) {
        Optional<Certificate> certOpt = certificateRepository.findByVerificationCode(code);
        
        VerificationLog log = VerificationLog.builder()
                .verifiedAt(LocalDateTime.now())
                .ipAddress(ipAddress)
                .build();

        if (certOpt.isPresent()) {
            log.setCertificate(certOpt.get());
            log.setStatus("SUCCESS");
        } else {
            log.setStatus("FAILED");
        }
        
        return logRepository.save(log);
    }

    @Override
    public List<VerificationLog> getLogsByCertificate(Long certificateId) {
        if (!certificateRepository.existsById(certificateId)) {
            throw new com.example.demo.exception.ResourceNotFoundException("Certificate not found");
        }
        return logRepository.findAll().stream()
                .filter(log -> log.getCertificate() != null && log.getCertificate().getId().equals(certificateId))
                .toList();
    }
}