package com.example.demo.service;

import com.example.demo.entity.Certificate;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CertificateRepository;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ResourceNotFoundException;


import java.util.List;

@Service
public class CertificateService {

    private final CertificateRepository repository;

    public CertificateService(CertificateRepository repository) {
        this.repository = repository;
    }

    public Certificate save(Certificate certificate) {
        return repository.save(certificate);
    }

    public List<Certificate> getAll() {
        return repository.findAll();
    }

    public Certificate getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
    }
}
