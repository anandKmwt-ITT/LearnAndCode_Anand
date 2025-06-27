package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.UserRequestDto;
import com.itt.newsAggregation.dto.UserResponseDto;

public interface UserService {
  UserResponseDto registerUser(UserRequestDto userDto);
}
