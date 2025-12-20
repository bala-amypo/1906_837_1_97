@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    User user = userService.findByEmail(request.getEmail());
    if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole()); // MANDATORY for the filter
        
        String token = jwtUtil.generateToken(claims, user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail(), user.getRole()));
    }
    return ResponseEntity.status(401).body("Invalid credentials");
}