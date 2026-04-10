package com.example.restapi.service;

import com.example.restapi.exception.UserNotFoundException;
import com.example.restapi.model.User;
import com.example.restapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        existing.setEmail(updatedUser.getEmail());
        existing.setPhone(updatedUser.getPhone());
        return userRepository.save(existing);
    }
}
