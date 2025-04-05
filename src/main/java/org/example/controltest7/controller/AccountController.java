package org.example.controltest7.controller;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.dto.AccountDto;
import org.example.controltest7.model.Account;
import org.example.controltest7.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestParam Long userId, @RequestParam String currency) {
        try {
            Account account = accountService.createAccount(userId, currency);
            return ResponseEntity.ok(accountService.toDto(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long accountId) {
        try {
            Account account = accountService.findById(accountId);
            return ResponseEntity.ok(accountService.toDto(account));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountDto>> getUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountService.findByUserId(userId);
        List<AccountDto> accountDtos = accounts.stream()
                .map(accountService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accountDtos);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long accountId) {
        try {
            Account account = accountService.findById(accountId);
            return ResponseEntity.ok(account.getBalance());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        try {
            Account account = accountService.deposit(accountId, amount);
            return ResponseEntity.ok(accountService.toDto(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}