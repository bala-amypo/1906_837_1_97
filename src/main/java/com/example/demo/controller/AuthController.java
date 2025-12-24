@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        User u = userService.findByEmail(req.getEmail());
        
        if (u != null && passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            java.util.Map<String, Object> claims = java.util.Map.of(
                    "userId", u.getId(),
                    "email", u.getEmail(),
                    "role", u.getRole()
            );
            String token = jwtUtil.generateToken(claims, u.getEmail());
            return ResponseEntity.ok(new com.example.demo.dto.AuthResponse(token, u.getId(), u.getEmail(), u.getRole()));
        }
        
        return ResponseEntity.status(401).build();
    }