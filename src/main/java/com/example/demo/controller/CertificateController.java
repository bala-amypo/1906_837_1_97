package com.example.demo.controller;

import com.example.demo.entity.Certificate;
import com.example.demo.service.CertificateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService service;

    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @PostMapping
    public Certificate create(@RequestBody Certificate certificate) {
        return service.save(certificate);
    }

    @GetMapping
    public List<Certificate> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Certificate getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
