package org.example.controltest7.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controltest7.dao.UserDao;
import org.example.controltest7.dto.UserRegistrationDto;
import org.example.controltest7.exception.UserAlreadyExistsException;
import org.example.controltest7.exception.UserNotFoundException;
import org.example.controltest7.model.User;
import org.example.controltest7.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerUser(UserRegistrationDto registrationDto) {
        if (userDao.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userDao.findByPhoneNumber(registrationDto.getPhoneNumber()).isPresent()) {
            throw new UserAlreadyExistsException("Phone number already exists");
        }

        User user = User.builder()
                .username(registrationDto.getUsername())
                .phoneNumber(registrationDto.getPhoneNumber())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .enabled(true)
                .build();

        userDao.create(user);

        Long userId = userDao.findByUsername(user.getUsername())
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve user after creation"));

        userDao.addUserAuthority(user.getUsername(), "ROLE_USER");

    }

    @Override
    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userDao.findAllUsers();
    }

    @Override
    @Transactional
    public void blockUser(String username, boolean blocked) {
        log.info("{}blocking user: {}", blocked ? "" : "Un", username);

        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        userDao.blockUser(username, blocked);
        log.info("User {} has been {}blocked", username, blocked ? "" : "un");
    }

}