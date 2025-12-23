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
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final StudentRepository studentRepository;
    private final CertificateTemplateRepository templateRepository;

    public CertificateServiceImpl(CertificateRepository cr, StudentRepository sr, CertificateTemplateRepository tr) {
        this.certificateRepository = cr; this.studentRepository = sr; this.templateRepository = tr;
    }

    @Override
    public Certificate generateCertificate(Long studentId, Long templateId) {
        Student s = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        CertificateTemplate t = templateRepository.findById(templateId).orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        String code = "VC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String qr;
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(code, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", bos);
            qr = "data:image/png;base64," + Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (Exception e) { throw new RuntimeException("QR Error"); }
        return certificateRepository.save(Certificate.builder().student(s).template(t).issuedDate(LocalDate.now()).verificationCode(code).qrCodeUrl(qr).build());
    }

    @Override public Certificate getCertificate(Long id) { return certificateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Certificate not found")); }
    @Override public Certificate findByVerificationCode(String c) { return certificateRepository.findByVerificationCode(c).orElseThrow(() -> new ResourceNotFoundException("Certificate not found")); }
    @Override public java.util.List<Certificate> findByStudentId(Long id) { 
        Student s = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return certificateRepository.findByStudent(s); 
    }
}