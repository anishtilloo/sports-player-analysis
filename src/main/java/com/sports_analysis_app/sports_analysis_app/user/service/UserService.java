package com.sports_analysis_app.sports_analysis_app.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser (String email, String name) {
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

        User user = new User();
        user.setEmail(email);
        user.setName(name);

        return userRepository.save(user);
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
