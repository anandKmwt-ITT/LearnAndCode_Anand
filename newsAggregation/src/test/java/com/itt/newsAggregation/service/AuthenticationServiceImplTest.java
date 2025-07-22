package com.itt.newsAggregation.service;

import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_ShouldReturnTrue_WhenCredentialsMatch() {
        String username = "john";
        String rawPassword = "password123";
        String encodedPassword = "encodedPwd";

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = authenticationService.authenticate(username, rawPassword);

        assertTrue(result);
        verify(userRepository).findByUsername(username);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }

    @Test
    void authenticate_ShouldReturnFalse_WhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        boolean result = authenticationService.authenticate("unknown", "password");

        assertFalse(result);
        verify(userRepository).findByUsername("unknown");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void authenticate_ShouldReturnFalse_WhenPasswordDoesNotMatch() {
        String username = "john";
        String rawPassword = "wrongPassword";
        String encodedPassword = "encodedPwd";

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        boolean result = authenticationService.authenticate(username, rawPassword);

        assertFalse(result);
        verify(userRepository).findByUsername(username);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }
}
