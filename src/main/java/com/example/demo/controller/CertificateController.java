package com.example.demo.controller;

import com.example.demo.entity.Certificate;
import com.example.demo.service.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/generate/{studentId}/{templateId}")
    public ResponseEntity<Certificate> generate(
            @PathVariable Long studentId,
            @PathVariable Long templateId) {

        return ResponseEntity.ok(
                certificateService.generateCertificate(studentId, templateId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getById(@PathVariable Long id) {
        return ResponseEntity.ok(certificateService.getCertificate(id));
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<Certificate> verify(@PathVariable String code) {
        return ResponseEntity.ok(
                certificateService.findByVerificationCode(code));
    }
}
