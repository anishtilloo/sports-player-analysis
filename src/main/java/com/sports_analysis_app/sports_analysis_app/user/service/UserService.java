package com.sports_analysis_app.sports_analysis_app.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sports_analysis_app.sports_analysis_app.common.exception.ResourceNotFoundException;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.repository.UserRepository;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUid(String uid) {
        User user = userRepository.findByUid(uid);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with uid: " + uid);
        }
        return user;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        log.debug("Fetched user by email: {}", email);
        return user;
    }
}
