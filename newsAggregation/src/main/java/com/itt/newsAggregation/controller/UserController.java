package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.UserRequestDto;
import com.itt.newsAggregation.dto.UserResponseDto;
import com.itt.newsAggregation.response.ApiResponse;
import com.itt.newsAggregation.response.AuthResponse;
import com.itt.newsAggregation.service.AuthenticationService;
import com.itt.newsAggregation.security.UserDetailsServiceImpl;
import com.itt.newsAggregation.service.UserService;
import com.itt.newsAggregation.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("register")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDto userDto){
        UserResponseDto savedUser = userService.registerUser(userDto);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .data(savedUser)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody UserRequestDto userDto) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            AuthResponse authResponse = AuthResponse.builder()
                    .token(jwt)
                    .role(userDetails.getAuthorities().stream().toList().get(0).getAuthority())
                    .build();
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }catch (AuthenticationException exception){
            log.error("Exception occurred while createAuthenticationToken ", exception);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
