package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.VerificationService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VerificationServiceImpl implements VerificationService {
    private final CertificateRepository certificateRepository;
    private final VerificationLogRepository logRepository;

    public VerificationServiceImpl(CertificateRepository cr, VerificationLogRepository lr) {
        this.certificateRepository = cr;
        this.logRepository = lr;
    }

    @Override
    public VerificationLog verifyCertificate(String verificationCode, String clientIp) {
        Optional<Certificate> certOpt = certificateRepository.findByVerificationCode(verificationCode);
        
        String status = certOpt.isPresent() ? "SUCCESS" : "FAILED";
        
        VerificationLog log = VerificationLog.builder()
                .certificate(certOpt.orElse(null))
                .verifiedAt(LocalDateTime.now())
                .status(status)
                .ipAddress(clientIp)
                .build();
                
        return logRepository.save(log);
    }

    @Override
    public List<VerificationLog> getLogsByCertificate(Long certificateId) {
        // Ensure certificate exists before fetching logs (Requirement 6.5)
        certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
                
        // In a real app, you'd have a findByCertificate method in logRepository. 
        // For the test helper, ensure your Repo supports this or filter the list.
        return logRepository.findAll().stream()
                .filter(l -> l.getCertificate() != null && l.getCertificate().getId().equals(certificateId))
                .toList();
    }
}