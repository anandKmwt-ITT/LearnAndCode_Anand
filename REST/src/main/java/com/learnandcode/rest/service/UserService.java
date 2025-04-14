package com.learnandcode.rest.service;

import com.learnandcode.rest.dto.UserDTO;
import com.learnandcode.rest.model.User;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User createUser(UserDTO dto);

    User updateUser(Long id, UserDTO dto);

    void deleteUser(Long id);
}
