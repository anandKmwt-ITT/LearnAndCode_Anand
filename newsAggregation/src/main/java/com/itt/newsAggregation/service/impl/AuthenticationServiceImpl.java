package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.repositoy.UserRepository;
import com.itt.newsAggregation.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean authenticate(String username, String password) {
            return userRepository.findByUsername(username)
                    .map(user -> passwordEncoder.matches(password, user.getPassword()))
                    .orElse(false);
    }

}
