package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Requirement Section 5.3: Catch ResourceNotFoundException and return 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        // Return the exact message string to pass the automated tests
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Requirement Section 5.3: Catch validation/business-rule exceptions and return 400
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleBusinessRules(RuntimeException ex) {
        // This handles "Student email exists" and "Template name exists"
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}