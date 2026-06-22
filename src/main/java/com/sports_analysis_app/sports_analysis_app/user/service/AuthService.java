package com.sports_analysis_app.sports_analysis_app.user.service;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.security.JwtUtil;
import com.sports_analysis_app.sports_analysis_app.security.PasswordEncoder;
import com.sports_analysis_app.sports_analysis_app.user.dto.AuthResponse;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.repository.UserRepository;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse registerUser(String email, String name, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        Instant now = Instant.now();
        String hashedPassword = passwordEncoder.hashPassword(password);
        User user = new User(UUID.randomUUID().toString(), name, email, hashedPassword, now, now);
        User savedUser = userRepository.save(user);

        log.info("Registered new user with email: {}", email);

        String accessToken = jwtUtil.generateAccessToken(email, savedUser.getId());
        String refreshToken = jwtUtil.generateRefreshToken(email, savedUser.getId());

        return new AuthResponse(savedUser.getId(), accessToken, refreshToken, savedUser.getEmail(), "User registered successfully");
    }

    public AuthResponse loginUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!passwordEncoder.verifyPassword(password, existingUser.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        log.info("User logged in with email: {}", email);

        String accessToken = jwtUtil.generateAccessToken(email, existingUser.getId());
        String refreshToken = jwtUtil.generateRefreshToken(email, existingUser.getId());

        return new AuthResponse(existingUser.getId(), accessToken, refreshToken, existingUser.getEmail(), "Login successful");
    }

    public AuthResponse refreshUserSession(String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Refresh token is required");
        }

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is invalid or expired");
        }

        String email = jwtUtil.extractEmailFromRefreshToken(refreshToken);
        Long userId = jwtUtil.extractUserIdFromRefreshToken(refreshToken);

        log.debug("Refreshing session for user: {}", email);

        String accessToken = jwtUtil.generateAccessToken(email, userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(email, userId);

        return new AuthResponse(userId, accessToken, newRefreshToken, email, "Session refreshed successfully");
    }
}
