package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // REQUIRED by StudentController
    public Student addStudent(Student student) {
        return repository.save(student);
    }

    // REQUIRED by StudentController
    public List<Student> getAllStudents() {
        return repository.findAll();
    }
}
