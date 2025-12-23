package com.example.demo.service.impl;

import com.example.demo.entity.CertificateTemplate;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CertificateTemplateRepository;
import com.example.demo.service.TemplateService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final CertificateTemplateRepository templateRepository;

    public TemplateServiceImpl(CertificateTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public CertificateTemplate addTemplate(CertificateTemplate template) {

        if (templateRepository.findByTemplateName(template.getTemplateName()).isPresent()) {
            throw new RuntimeException("Template name exists");
        }

        return templateRepository.save(template);
    }

    @Override
    public List<CertificateTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }

    @Override
    public CertificateTemplate findById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
    }
}
