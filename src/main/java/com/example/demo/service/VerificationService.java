package com.example.demo.service;

import com.example.demo.entity.VerificationLog;
import java.util.List;

public interface VerificationService {
    VerificationLog verifyCertificate(String code, String ip);
    List<VerificationLog> getLogsByCertificate(Long certificateId);
}
