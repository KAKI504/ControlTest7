package org.example.controltest7.controller;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.dto.UserRegisterDto;
import org.example.controltest7.model.User;
import org.example.controltest7.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterDto registerDto) {
        try {
            User user = userService.registerUser(registerDto);
            return ResponseEntity.ok("Пользователь успешно зарегистрирован, ID: " + user.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API работает!");
    }
}