package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.CertificateService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final StudentRepository studentRepository;
    private final CertificateTemplateRepository templateRepository;

    public CertificateServiceImpl(CertificateRepository certificateRepository, 
                                  StudentRepository studentRepository, 
                                  CertificateTemplateRepository templateRepository) {
        this.certificateRepository = certificateRepository;
        this.studentRepository = studentRepository;
        this.templateRepository = templateRepository;
    }

    @Override
    public Certificate generateCertificate(Long studentId, Long templateId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        CertificateTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        // Section 6.4 Rule: Verification code starts with VC-
        String vCode = "VC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        String qrBase64 = "";
        try {
            // QR Generation logic placed here inside the Service
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(vCode, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
            qrBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR code");
        }

        Certificate certificate = Certificate.builder()
                .student(student)
                .template(template)
                .issuedDate(LocalDate.now())
                .verificationCode(vCode)
                .qrCodeUrl(qrBase64)
                .build();

        return certificateRepository.save(certificate);
    }

    @Override
    public Certificate getCertificate(Long id) {
        return certificateRepository.findById(id).orElseThrow(() -> 
            new ResourceNotFoundException("Certificate not found")); // EXACT STRING
    }

    @Override
    public Certificate findByVerificationCode(String code) {
        return certificateRepository.findByVerificationCode(code).orElseThrow(() -> 
            new ResourceNotFoundException("Certificate not found")); // EXACT STRING
    }

    @Override
    public List<Certificate> findByStudentId(Long studentId) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return certificateRepository.findByStudent(s);
    }
}