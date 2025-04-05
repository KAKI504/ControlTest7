package org.example.controltest7.service;

import org.example.controltest7.dto.UserRegisterDto;
import org.example.controltest7.model.User;

public interface UserService {
    User registerUser(UserRegisterDto registerDto);
    User findByUsername(String username);
    User findById(Long id);
    boolean existsByUsername(String username);
}