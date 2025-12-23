package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication Management")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Requirement Section 6: Constructor injection only
    public AuthController(
            UserService userService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<User> register(
            @RequestBody RegisterRequest request
    ) {
        // Section 7.1: Convert DTO to Entity and delegate to service
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    @Operation(summary = "User login to get JWT token")
    public ResponseEntity<?> login(
            @RequestBody AuthRequest request
    ) {
        // 1. Look up the user by email
        User user = userService.findByEmail(request.getEmail());

        // 2. Validate the password using matches()
        if (user != null
                && passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                )
        ) {

            // 3. Build claim map exactly as required in Section 7.1
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("email", user.getEmail());
            claims.put("role", user.getRole());

            // 4. Generate the token
            String token = jwtUtil.generateToken(
                    claims,
                    user.getEmail()
            );

            // 5. Wrap in AuthResponse and return 200 OK
            AuthResponse response = new AuthResponse(
                    token,
                    user.getId(),
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok(response);
        }

        // 6. Return 401 Unauthorized for invalid credentials
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
    }
}
