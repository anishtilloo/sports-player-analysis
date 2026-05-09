package com.sports_analysis_app.sports_analysis_app.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.security.JwtUtil;
import com.sports_analysis_app.sports_analysis_app.security.PasswordEncoder;
import com.sports_analysis_app.sports_analysis_app.user.dto.AuthResponse;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    public AuthResponse registerUser (String email, String name, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is Required");
        }

        User existingUser = userRepository.findByEmail(email);

        if (existingUser != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is Required");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is Required");
        }

        long now = System.currentTimeMillis();
        String hashedPassword = passwordEncoder.hashPassword(password);
        User user = new User(name, email, hashedPassword, now, now);
        User saveUser = userRepository.save(user);

        String token = jwtUtil.generateToken(email, saveUser.getId());

        return new AuthResponse(saveUser.getId(), token, saveUser.getEmail(), "User registered successfully");
    }

    public AuthResponse loginUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is Required");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is Required");
        }

        User existingUser = userRepository.findByEmail(email);

        if (existingUser == null) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (!passwordEncoder.verifyPassword(password, existingUser.getPassword())) {
            throw new IllegalArgumentException("Invalid Email or Password");
        }

        String token = jwtUtil.generateToken(email, existingUser.getId());

        return new AuthResponse(existingUser.getId(), token, existingUser.getEmail(), "Login Successful");
    }

    public User getUserById (Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUserByEmail (String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }
}
