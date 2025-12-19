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

    
    private String status;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;
}
