package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.request.UserRequestDto;
import com.itt.newsAggregation.dto.response.UserResponseDto;

public interface UserService {
  UserResponseDto registerUser(UserRequestDto userDto);
  UserResponseDto getUserByUsername(String username);
}
