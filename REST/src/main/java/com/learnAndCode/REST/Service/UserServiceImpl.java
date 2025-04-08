package com.learnAndCode.REST.Service;

import com.learnAndCode.REST.Dto.UserDTO;
import com.learnAndCode.REST.Model.User;
import com.learnAndCode.REST.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(UserDTO dto) {
        return userRepository.save(mapToEntity(dto));
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
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
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
}
