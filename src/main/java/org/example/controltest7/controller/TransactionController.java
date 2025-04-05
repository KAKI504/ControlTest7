package org.example.controltest7.controller;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.dto.TransactionDto;
import org.example.controltest7.model.Transaction;
import org.example.controltest7.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount) {
        try {
            Transaction transaction = transactionService.createTransaction(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok(transactionService.toDto(transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long transactionId) {
        try {
            Transaction transaction = transactionService.findById(transactionId);
            return ResponseEntity.ok(transactionService.toDto(transaction));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionDto>> getAccountTransactions(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.findByAccountId(accountId);
        List<TransactionDto> transactionDtos = transactions.stream()
                .map(transactionService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transactionDtos);
    }
}