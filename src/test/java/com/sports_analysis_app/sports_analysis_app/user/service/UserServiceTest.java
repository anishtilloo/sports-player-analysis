package com.sports_analysis_app.sports_analysis_app.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sports_analysis_app.sports_analysis_app.common.exception.ResourceNotFoundException;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserByEmail_found() {
        User user = new User("uid", "Test User", "test@example.com", "hashed", null, null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.getUserByEmail("test@example.com");

        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getUserByEmail_notFound_throwsException() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByEmail("missing@example.com"));
    }

    @Test
    void getUserByUid_found() {
        User user = new User("abc-uid", "Test User", "test@example.com", "hashed", null, null);
        when(userRepository.findByUid("abc-uid")).thenReturn(user);

        User result = userService.getUserByUid("abc-uid");

        assertEquals("abc-uid", result.getUid());
    }

    @Test
    void getUserByUid_notFound_throwsException() {
        when(userRepository.findByUid("bad-uid")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByUid("bad-uid"));
    }
}
