package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User register(User user) {

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        return repository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
