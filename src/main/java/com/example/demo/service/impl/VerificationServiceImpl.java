package com.example.demo.service.impl;

import com.example.demo.entity.Certificate;
import com.example.demo.entity.VerificationLog;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.VerificationLogRepository;
import com.example.demo.service.VerificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final VerificationLogRepository logRepository;
    private final CertificateRepository certificateRepository;

    // ✅ Constructor injection only
    public VerificationServiceImpl(
            VerificationLogRepository logRepository,
            CertificateRepository certificateRepository
    ) {
        this.logRepository = logRepository;
        this.certificateRepository = certificateRepository;
    }

    // ================= VERIFY CERTIFICATE =================
    @Override
    public VerificationLog verifyCertificate(String code, String ipAddress) {

        Optional<Certificate> certOpt =
                certificateRepository.findByVerificationCode(code);

        VerificationLog log = VerificationLog.builder()
                .verifiedAt(LocalDateTime.now()) // MUST NOT be null
                .ipAddress(ipAddress)
                .build();

        if (certOpt.isPresent()) {
            log.setCertificate(certOpt.get());
            log.setStatus("SUCCESS");
        } else {
            // ❗ DO NOT throw exception (required by Helper PDF)
            log.setStatus("FAILED");
        }

        return logRepository.save(log);
    }

    // ================= GET LOGS BY CERTIFICATE =================
    @Override
    public List<VerificationLog> getLogsByCertificate(Long certificateId) {

        // Must return empty list if no logs found (NO exception)
        return logRepository.findAll()
                .stream()
                .filter(log ->
                        log.getCertificate() != null &&
                        log.getCertificate().getId().equals(certificateId)
                )
                .collect(Collectors.toList());
    }
}
