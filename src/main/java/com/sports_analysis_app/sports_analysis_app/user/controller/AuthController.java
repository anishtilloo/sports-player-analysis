package com.sports_analysis_app.sports_analysis_app.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports_analysis_app.sports_analysis_app.user.dto.AuthResponse;
import com.sports_analysis_app.sports_analysis_app.user.dto.LoginRequest;
import com.sports_analysis_app.sports_analysis_app.user.dto.RefreshRequest;
import com.sports_analysis_app.sports_analysis_app.user.dto.RegisterRequest;
import com.sports_analysis_app.sports_analysis_app.user.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.registerUser(request.getEmail(), request.getName(), request.getPassword());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.loginUser(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        return authService.refreshUserSession(request.getRefreshToken());
    }
}
