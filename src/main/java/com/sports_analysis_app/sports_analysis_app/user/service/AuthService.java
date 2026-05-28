package com.sports_analysis_app.sports_analysis_app.user.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;

import com.sports_analysis_app.sports_analysis_app.security.JwtUtil;
import com.sports_analysis_app.sports_analysis_app.security.PasswordEncoder;
import com.sports_analysis_app.sports_analysis_app.user.dto.AuthResponse;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.repository.UserRepository;

public class AuthService {
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

        Instant now = Instant.now();
        String hashedPassword = passwordEncoder.hashPassword(password);
        User user = new User("1234565432", name, email, hashedPassword, now, now);
        User saveUser = userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(email, saveUser.getId());
        String refreshToken = jwtUtil.generateRefreshToken(email, saveUser.getId());

        return new AuthResponse(saveUser.getId(), accessToken, refreshToken, saveUser.getEmail(), "User registered successfully");
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

        String accessToken = jwtUtil.generateAccessToken(email, existingUser.getId());
        String refreshToken = jwtUtil.generateRefreshToken(email, existingUser.getId());

        return new AuthResponse(existingUser.getId(), accessToken,  refreshToken, existingUser.getEmail(), "Login Successful");
    }

    public AuthResponse refreshUserSession(String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Refresh Token is Required");
        }

        boolean isRefreshTokenVerified = jwtUtil.validateRefreshToken(refreshToken);

        if (!isRefreshTokenVerified) {
            throw new IllegalArgumentException("Refresh Token not verified");
        }

        String email = jwtUtil.extractEmailFromRefreshToken(refreshToken);
        Long userId = jwtUtil.extractUserIdFromRefreshToken(refreshToken);

        String accessToken = jwtUtil.generateAccessToken(email, userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(email, userId);

        return new AuthResponse(userId, accessToken,  newRefreshToken, email, "User Session Refresh Successful");

    }

}
