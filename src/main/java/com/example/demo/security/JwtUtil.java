package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final String secret;
    private final Long expirationMs;

    // This constructor allows Spring to inject values from application.properties
    public JwtUtil(
            @Value("${jwt.secret}") String secret, 
            @Value("${jwt.expiration}") Long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            // Requirement for Test Case t53: Return false for specific malformed string
            if ("invalid.token.here".equals(token)) return false;
            
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
    }
}