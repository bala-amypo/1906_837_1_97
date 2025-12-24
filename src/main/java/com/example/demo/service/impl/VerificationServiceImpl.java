package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.VerificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final CertificateRepository certificateRepository;
    private final VerificationLogRepository logRepository;

    public VerificationServiceImpl(CertificateRepository c, VerificationLogRepository v) {
        this.certificateRepository = c;
        this.logRepository = v;
    }

    @Override
    public VerificationLog verify(String code, String ip) {
        Certificate cert = certificateRepository.findByVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        VerificationLog log = new VerificationLog();
        log.setCertificate(cert);
        log.setIpAddress(ip);
        log.setStatus("SUCCESS");
        log.setVerifiedAt(LocalDateTime.now());

        return logRepository.save(log);
    }

    @Override
    public List<VerificationLog> logs() {
        return logRepository.findAll();
    }
}
