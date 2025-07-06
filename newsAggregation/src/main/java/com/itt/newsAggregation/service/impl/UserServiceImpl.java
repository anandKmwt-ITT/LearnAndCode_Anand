package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.request.UserRequestDto;
import com.itt.newsAggregation.dto.response.UserResponseDto;
import com.itt.newsAggregation.exception.UserAlreadyExistsException;
import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.NotificationPreference;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.repository.NotificationPreferenceRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;


    @Override
    public UserResponseDto registerUser(UserRequestDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + userDto.getUsername());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + userDto.getEmail());
        }
        User user = userDtoToUser.apply(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        setDefaultNotificationPreferences(savedUser);
        return userToUserResponseDto.apply(savedUser);
    }

    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public static final Function<UserRequestDto, User> userDtoToUser = userDto -> User.builder()
            .username(userDto.getUsername())
            .email(userDto.getEmail())
            .password(userDto.getPassword())
            .build();

    public static final Function<User, UserResponseDto> userToUserResponseDto = user -> UserResponseDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();

    private void setDefaultNotificationPreferences(User user) {
        List<Category> allCategories = categoryRepository.findAll();

        List<NotificationPreference> defaultPreferences = allCategories.stream()
                .map(category -> NotificationPreference.builder()
                        .user(user)
                        .category(category)
                        .enabled(false)
                        .build())
                .collect(Collectors.toList());

        notificationPreferenceRepository.saveAll(defaultPreferences);
    }
}