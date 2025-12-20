package com.example.demo.service.impl;

import com.example.demo.entity.Student;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addStudent(Student student) {
        // Section 6.2 Rule: Check both email and rollNumber
        if (studentRepository.findByEmail(student.getEmail()).isPresent() || 
            studentRepository.findByRollNumber(student.getRollNumber()).isPresent()) {
            throw new RuntimeException("Student email exists"); // EXACT STRING REQUIRED
        }
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> 
            new ResourceNotFoundException("Student not found")); // EXACT STRING REQUIRED
    }
}