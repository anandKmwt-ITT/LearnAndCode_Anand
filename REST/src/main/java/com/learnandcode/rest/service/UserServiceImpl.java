package com.learnandcode.rest.service;

import com.learnandcode.rest.dto.UserDTO;
import com.learnandcode.rest.exception.ResourceConflictException;
import com.learnandcode.rest.exception.UserNotFoundException;
import com.learnandcode.rest.model.User;
import com.learnandcode.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found with ID: " + id));
    }

    @Override
    public User createUser(UserDTO userDTO) {
        checkForConflicts(userDTO);
        return userRepository.save(mapToEntity(userDTO));
    }

    @Override
    public User updateUser(Long id, UserDTO dto) {
        return userRepository.findById(id).map(user -> {
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setAge(dto.getAge());
            user.setAddress(dto.getAddress());
            user.setPhoneNumber(dto.getPhoneNumber());
            return userRepository.save(user);
        }).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    private User mapToEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        return user;
    }

    private void checkForConflicts(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceConflictException("Email already in use: " + userDTO.getEmail());
        }
        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new ResourceConflictException("Phone number already in use: " + userDTO.getPhoneNumber());
        }
    }
}
