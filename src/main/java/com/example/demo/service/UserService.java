package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    User register(User user);
    // Change this from Optional<User> to User
    User findByEmail(String email);
}