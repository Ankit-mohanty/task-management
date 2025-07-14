package com.example.backend.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.LoginDto;
import com.example.backend.dto.RegisterDto;
import com.example.backend.entity.User;
import com.example.backend.repo.UserRepository;
import com.example.backend.utils.JwtUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public Map<String, String> register(RegisterDto registerDto) {
        Map<String, String> response = new HashMap<>();
        
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            response.put("error", "Email already exists");
            return response;
        }
        
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        response.put("message", "User registered successfully");
        return response;
    }
    
    public Map<String, String> login(LoginDto loginDto) {
        Map<String, String> response = new HashMap<>();
        
        Optional<User> userOptional = userRepository.findByEmail(loginDto.getEmail());
        
        if (userOptional.isEmpty()) {
            response.put("error", "User not found");
            return response;
        }
        
        User user = userOptional.get();
        
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            response.put("error", "Invalid credentials");
            return response;
        }
        
        String token = jwtUtil.generateToken(user.getEmail());
        response.put("token", token);
        response.put("message", "Login successful");
        
        return response;
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}