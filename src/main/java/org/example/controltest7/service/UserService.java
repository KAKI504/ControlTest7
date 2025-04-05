package org.example.controltest7.service;

import org.example.controltest7.dto.UserRegistrationDto;
import org.example.controltest7.model.User;

import java.util.List;

public interface UserService {
    void registerUser(UserRegistrationDto registrationDto);
    List<User> getAllUsers();
    void blockUser(String username, boolean blocked);
}