package com.example.demo.controller;

import com.example.demo.entity.Certificate;
import com.example.demo.service.CertificateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificates")
@Tag(
        name = "Certificate",
        description = "Certificate Generation and Retrieval"
)
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(
            CertificateService certificateService
    ) {
        this.certificateService = certificateService;
    }

    @PostMapping("/generate/{studentId}/{templateId}")
    @Operation(summary = "Generate a certificate")
    public ResponseEntity<Certificate> generateCertificate(
            @PathVariable Long studentId,
            @PathVariable Long templateId
    ) {
        return ResponseEntity.ok(
                certificateService.generateCertificate(
                        studentId,
                        templateId
                )
        );
    }

    @GetMapping("/{certificateId}")
    @Operation(summary = "Get certificate by ID")
    public ResponseEntity<Certificate> getCertificate(
            @PathVariable Long certificateId
    ) {
        return ResponseEntity.ok(
                certificateService.getCertificate(
                        certificateId
                )
        );
    }

    @GetMapping("/verify/code/{verificationCode}")
    @Operation(summary = "Find certificate by verification code")
    public ResponseEntity<Certificate> findByVerificationCode(
            @PathVariable String verificationCode
    ) {
        return ResponseEntity.ok(
                certificateService.findByVerificationCode(
                        verificationCode
                )
        );
    }
}
