package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String verifiedBy;

    private LocalDateTime verifiedAt;

    // REQUIRED by VerificationServiceImpl & testcases
    private String status;

    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;
}
