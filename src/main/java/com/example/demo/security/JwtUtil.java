package com.example.demo.security;

public class JwtUtil {

    private String secret;
    private long expirationMs;

    public JwtUtil(String secret, long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }
}
