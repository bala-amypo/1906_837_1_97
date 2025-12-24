package com.example.demo.service.impl;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.CertificateService;
import com.example.demo.exception.ResourceNotFoundException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;

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
        
        // Rule: Start with VC- (t28)
        String vCode = "VC-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        
        // Rule: Start with data:image/png;base64, (t29)
        String qr = generateQR(vCode);

        Certificate cert = Certificate.builder()
                .student(s).template(t).issuedDate(LocalDate.now())
                .verificationCode(vCode).qrCodeUrl(qr).build();
        return certificateRepository.save(cert);
    }

    private String generateQR(String text) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            var matrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", os);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (Exception e) { return "data:image/png;base64,err"; }
    }

    @Override
    public Certificate getCertificate(Long id) {
        return certificateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
    }

    @Override
    public Certificate findByVerificationCode(String code) {
        return certificateRepository.findByVerificationCode(code).orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
    }

    @Override
    public List<Certificate> findByStudentId(Long sid) {
        Student s = studentRepository.findById(sid).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return certificateRepository.findByStudent(s);
    }
}