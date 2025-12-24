package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity @Table(name = "students")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String rollNumber;
    @OneToMany(mappedBy = "student")
    private List<Certificate> certificates;
}