package org.example.controltest7.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.controltest7.dto.TransactionCreateDto;
import org.example.controltest7.model.Transaction;
import org.example.controltest7.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<String> createTransaction(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionCreateDto transactionDto) {
        transactionService.createTransaction(userDetails.getUsername(), accountId, transactionDto);
        return ResponseEntity.ok("Transaction created successfully");
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getAccountTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getAccountTransactions(userDetails.getUsername(),
                accountId);
        return ResponseEntity.ok(transactions);
    }
}