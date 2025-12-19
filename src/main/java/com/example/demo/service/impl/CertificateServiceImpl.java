package com.example.demo.service.impl;

import com.example.demo.entity.Certificate;
import com.example.demo.entity.Student;
import com.example.demo.entity.CertificateTemplate;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.CertificateTemplateRepository;
import com.example.demo.service.CertificateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepo;
    private final StudentRepository studentRepo;
    private final CertificateTemplateRepository templateRepo;

    public CertificateServiceImpl(CertificateRepository certificateRepo,
                                  StudentRepository studentRepo,
                                  CertificateTemplateRepository templateRepo) {
        this.certificateRepo = certificateRepo;
        this.studentRepo = studentRepo;
        this.templateRepo = templateRepo;
    }

    @Override
    public Certificate generateCertificate(Long studentId, Long templateId) {

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        CertificateTemplate template = templateRepo.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        String verificationCode = "VC-" + UUID.randomUUID();

        String qrCodeUrl = "data:image/png;base64," +
                Base64.getEncoder().encodeToString(verificationCode.getBytes());

        Certificate certificate = Certificate.builder()
                .student(student)
                .template(template)
                .issuedDate(LocalDate.now())
                .verificationCode(verificationCode)
                .qrCodeUrl(qrCodeUrl)
                .build();

        return certificateRepo.save(certificate);
    }

    @Override
    public Certificate getCertificate(Long certificateId) {
        return certificateRepo.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
    }

    @Override
    public Certificate findByVerificationCode(String code) {
        return certificateRepo.findByVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
    }

    @Override
    public List<Certificate> findByStudentId(Long studentId) {

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return certificateRepo.findByStudent(student);
    }
}
