package com.example.demo.controller;

import com.example.demo.dto.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        return new AuthResponse("dummy-token");
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        return "User registered";
    }
}
