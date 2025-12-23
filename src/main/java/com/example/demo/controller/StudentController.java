package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@Tag(
        name = "Student",
        description = "Student Management"
)
public class StudentController {

    private final StudentService studentService;

    public StudentController(
            StudentService studentService
    ) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Add a new student")
    public ResponseEntity<Student> addStudent(
            @RequestBody Student student
    ) {
        return ResponseEntity.ok(
                studentService.addStudent(student)
        );
    }

    @GetMapping
    @Operation(summary = "Get all students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(
                studentService.getAllStudents()
        );
    }
}
