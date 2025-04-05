package org.example.controltest7.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.controltest7.dto.AccountCreateDto;
import org.example.controltest7.model.Account;
import org.example.controltest7.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<String> createAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AccountCreateDto accountCreateDto) {
        accountService.createAccount(userDetails.getUsername(), accountCreateDto);
        return ResponseEntity.ok("Account created successfully");
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        List<Account> accounts = accountService.getUserAccounts(userDetails.getUsername());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/accounts/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long accountId) {
        BigDecimal balance = accountService.getAccountBalance(userDetails.getUsername(), accountId);
        return ResponseEntity.ok(balance);
    }
}