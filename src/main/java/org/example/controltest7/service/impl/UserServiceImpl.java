package org.example.controltest7.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.dao.UserDao;
import org.example.controltest7.dto.UserRegisterDto;
import org.example.controltest7.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements org.example.controltest7.service.UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegisterDto registerDto) {
        if (userDao.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setEnabled(true);

        Long userId = userDao.saveAndGetId(user);
        user.setId(userId);

        userDao.saveAuthority(registerDto.getUsername(), "ROLE_USER");

        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @Override
    public User findById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userDao.existsByUsername(username);
    }
}