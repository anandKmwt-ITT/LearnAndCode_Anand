package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.UserDto;
import com.itt.newsAggregation.exception.UserAlreadyExistsException;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repositoy.UserRepository;
import com.itt.newsAggregation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + userDto.getUsername());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + userDto.getEmail());
        }
        User user = userDtoToUser.apply(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return userToUserDto.apply(saved);
    }

    public static final Function<UserDto, User> userDtoToUser = userDto -> User.builder()
            .username(userDto.getUsername())
            .email(userDto.getEmail())
            .password(userDto.getPassword())
            .build();

    public static final Function<User, UserDto> userToUserDto = user -> UserDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .build();
}
