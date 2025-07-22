package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.request.UserRequestDto;
import com.itt.newsAggregation.dto.response.UserResponseDto;
import com.itt.newsAggregation.exception.UserAlreadyExistsException;
import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.repository.NotificationPreferenceRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private CategoryRepository categoryRepository;
    @Mock private NotificationPreferenceRepository notificationPreferenceRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDto requestDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        requestDto = UserRequestDto.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("plainPassword")
                .build();
    }

    @Test
    void registerUser_successfulRegistration() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);

        User userToSave = UserServiceImpl.userDtoToUser.apply(requestDto);
        User savedUser = User.builder()
                .id(1)
                .username("john_doe")
                .email("john@example.com")
                .password("encodedPassword")
                .build();

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(categoryRepository.findAll()).thenReturn(List.of());

        UserResponseDto response = userService.registerUser(requestDto);

        assertEquals("john_doe", response.getUsername());
        assertEquals("john@example.com", response.getEmail());

        verify(notificationPreferenceRepository).saveAll(anyList());
    }

    @Test
    void registerUser_usernameExists_throwsException() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);
        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(requestDto));
        assertEquals("Username already exists: john_doe", ex.getMessage());
    }

    @Test
    void registerUser_emailExists_throwsException() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(requestDto));
        assertEquals("Email already exists: john@example.com", ex.getMessage());
    }

    @Test
    void getUserByUsername_success() {
        User user = User.builder()
                .id(1)
                .username("john_doe")
                .email("john@example.com")
                .build();

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        UserResponseDto response = userService.getUserByUsername("john_doe");

        assertEquals(1, response.getId());
        assertEquals("john_doe", response.getUsername());
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void getUserByUsername_notFound_throwsException() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.getUserByUsername("john_doe"));
        assertEquals("User not found", ex.getMessage());
    }
}