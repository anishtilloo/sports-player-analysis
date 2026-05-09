package com.sports_analysis_app.sports_analysis_app.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.dto.RegisterRequest;
import com.sports_analysis_app.sports_analysis_app.user.dto.AuthResponse;
import com.sports_analysis_app.sports_analysis_app.user.dto.LoginRequest;
import com.sports_analysis_app.sports_analysis_app.user.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return service.registerUser(request.getEmail(), request.getName(), request.getPassword());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return service.loginUser(request.getEmail(), request.getPassword());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public User getUser(@PathVariable String email) {
        return service.getUserByEmail(email);
    }
}
