package com.example.demo.service;

import com.example.demo.entity.Certificate;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CertificateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {

    private final CertificateRepository repository;

    public CertificateService(CertificateRepository repository) {
        this.repository = repository;
    }

    // Create / Save certificate
    public Certificate addCertificate(Certificate certificate) {
        return repository.save(certificate);
    }

    // Get all certificates
    public List<Certificate> getAllCertificates() {
        return repository.findAll();
    }

    // Get certificate by ID
    public Certificate getCertificateById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Certificate not found with id: " + id)
                );
    }
}
