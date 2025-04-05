package org.example.controltest7.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controltest7.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final UserService userService;

    @PostMapping("/{username}/block")
    public ResponseEntity<String> blockUser(@PathVariable String username) {
        userService.blockUser(username, true);
        return ResponseEntity.ok("User blocked successfully");
    }

    @PostMapping("/{username}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable String username) {
        userService.blockUser(username, false);
        return ResponseEntity.ok("User unblocked successfully");
    }
}