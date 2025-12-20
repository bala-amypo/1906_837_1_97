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

    // Section 6 Rule: Constructor injection only, no field-level @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        // Section 7.1: Convert RegisterRequest to User entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) // Raw password passed; service will hash it
                .role(request.getRole())
                .build();
        
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    @Operation(summary = "User login to get JWT token")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Step 2.1: Use userService.findByEmail(email) to look up the user
        User user = userService.findByEmail(request.getEmail());

        // Step 2.2: Validate the password using BCryptPasswordEncoder.matches(...)
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            
            // Step 2.2.1: On success, build claim map (userId, email, role)
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("email", user.getEmail());
            claims.put("role", user.getRole());

            // Step 2.2.1: Call jwtUtil.generateToken(claims, email)
            String token = jwtUtil.generateToken(claims, user.getEmail());

            // Step 2.2.2: Wrap the token and user details in AuthResponse and return HTTP 200
            AuthResponse authResponse = new AuthResponse(
                    token, 
                    user.getId(), 
                    user.getEmail(), 
                    user.getRole()
            );
            
            return ResponseEntity.ok(authResponse);
        }

        // Step 2.2.3: On invalid credentials, return HTTP 401 with an appropriate message
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}