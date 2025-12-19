package com.example.demo.service.impl;

import com.example.demo.entity.VerificationLog;
import com.example.demo.entity.Certificate;
import com.example.demo.repository.VerificationLogRepository;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.service.VerificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final VerificationLogRepository logRepo;
    private final CertificateRepository certificateRepo;

    public VerificationServiceImpl(VerificationLogRepository logRepo,
                                   CertificateRepository certificateRepo) {
        this.logRepo = logRepo;
        this.certificateRepo = certificateRepo;
    }

    @Override
    public VerificationLog verifyCertificate(String code, String ip) {

        Certificate cert = certificateRepo.findByVerificationCode(code).orElse(null);

        VerificationLog log = VerificationLog.builder()
                .certificate(cert)
                .verifiedAt(LocalDateTime.now())
                .status(cert != null ? "SUCCESS" : "FAILED")
                .ipAddress(ip)
                .build();

        return logRepo.save(log);
    }

    @Override
    public List<VerificationLog> getLogsByCertificate(Long certificateId) {

        Certificate cert = certificateRepo.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        return cert.getVerificationLogs();
    }
}
