package com.learnAndCode.REST.Service;

import com.learnAndCode.REST.Dto.UserDTO;
import com.learnAndCode.REST.Model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User createUser(UserDTO dto);

    User updateUser(Long id, UserDTO dto);

    void deleteUser(Long id);
}
