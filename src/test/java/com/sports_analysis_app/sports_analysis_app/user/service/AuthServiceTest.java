package com.sports_analysis_app.sports_analysis_app.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sports_analysis_app.sports_analysis_app.security.JwtUtil;
import com.sports_analysis_app.sports_analysis_app.security.PasswordEncoder;
import com.sports_analysis_app.sports_analysis_app.user.dto.AuthResponse;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(passwordEncoder.hashPassword("password123")).thenReturn("hashed");
        User saved = new User("uid", "Test User", "test@example.com", "hashed", null, null);
        // Simulate the saved user having an id
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(jwtUtil.generateAccessToken(any(), any())).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(any(), any())).thenReturn("refresh-token");

        AuthResponse response = authService.registerUser("test@example.com", "Test User", "password123");

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("User registered successfully", response.getMessage());
    }

    @Test
    void registerUser_emailAlreadyExists_throwsException() {
        User existing = new User("uid", "Existing", "test@example.com", "hashed", null, null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(existing);

        assertThrows(IllegalArgumentException.class,
                () -> authService.registerUser("test@example.com", "Test User", "password123"));
    }

    @Test
    void registerUser_blankEmail_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.registerUser("", "Test User", "password123"));
    }

    @Test
    void loginUser_success() {
        User existing = new User("uid", "Test User", "test@example.com", "hashed", null, null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(existing);
        when(passwordEncoder.verifyPassword("password123", "hashed")).thenReturn(true);
        when(jwtUtil.generateAccessToken(any(), any())).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(any(), any())).thenReturn("refresh-token");

        AuthResponse response = authService.loginUser("test@example.com", "password123");

        assertNotNull(response);
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void loginUser_emailNotFound_throwsException() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> authService.loginUser("missing@example.com", "password123"));
    }

    @Test
    void loginUser_wrongPassword_throwsException() {
        User existing = new User("uid", "Test User", "test@example.com", "hashed", null, null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(existing);
        when(passwordEncoder.verifyPassword("wrongpass", "hashed")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.loginUser("test@example.com", "wrongpass"));
    }

    @Test
    void refreshUserSession_invalidToken_throwsException() {
        when(jwtUtil.validateRefreshToken("bad-token")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.refreshUserSession("bad-token"));
    }

    @Test
    void refreshUserSession_blankToken_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.refreshUserSession(""));
    }
}
