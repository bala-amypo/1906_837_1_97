package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor injection (required)
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user (Admin or Staff)")
    public ResponseEntity<User> register(@RequestBody RegisterRequest req) {
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(req.getPassword())
                .role(req.getRole())
                .build();
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    @Operation(summary = "Login to obtain a JWT token")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        return userService.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .map(u -> {
                    Map<String, Object> claims = Map.of(
                            "userId", u.getId(),
                            "email", u.getEmail(),
                            "role", u.getRole()
                    );
                    String token = jwtUtil.generateToken(claims, u.getEmail());
                    return ResponseEntity.ok(new AuthResponse(token, u.getId(), u.getEmail(), u.getRole()));
                })
                .orElse(ResponseEntity.status(401).build()); // Returns 401 for invalid credentials
    }
}