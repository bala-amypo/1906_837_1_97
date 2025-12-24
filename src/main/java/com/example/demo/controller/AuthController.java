package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    // Use constructor injection as required by project rules
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(req.getPassword())
                .role(req.getRole())
                .build();
        User saved = userService.register(user);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        // Logic modified to fix the previous 'symbol not found getId()' error
        User u = userService.findByEmail(req.getEmail());

        if (u != null && passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            Map<String, Object> claims = Map.of(
                    "userId", u.getId(),
                    "email", u.getEmail(),
                    "role", u.getRole()
            );
            String token = jwtUtil.generateToken(claims, u.getEmail());
            // Returns AuthResponse DTO to satisfy Section 3.2
            return ResponseEntity.ok(new AuthResponse(token, u.getId(), u.getEmail(), u.getRole()));
        }

        // Returns HTTP 401 for invalid credentials (t50 requirement)
        return ResponseEntity.status(401).build();
    }
}