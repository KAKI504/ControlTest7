package org.example.controltest7.controller;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.dto.TransactionDto;
import org.example.controltest7.model.Transaction;
import org.example.controltest7.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        List<Transaction> transactions = transactionService.findAllTransactions();
        List<TransactionDto> transactionDtos = transactions.stream()
                .map(transactionService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transactionDtos);
    }

    @GetMapping("/transactions/pending")
    public ResponseEntity<List<TransactionDto>> getPendingTransactions() {
        List<Transaction> transactions = transactionService.findPendingTransactions();
        List<TransactionDto> transactionDtos = transactions.stream()
                .map(transactionService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transactionDtos);
    }

    @PostMapping("/transactions/{transactionId}/approve")
    public ResponseEntity<TransactionDto> approveTransaction(@PathVariable Long transactionId) {
        try {
            Transaction transaction = transactionService.approveTransaction(transactionId);
            return ResponseEntity.ok(transactionService.toDto(transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/transactions/{transactionId}/reject")
    public ResponseEntity<TransactionDto> rejectTransaction(@PathVariable Long transactionId) {
        try {
            Transaction transaction = transactionService.rejectTransaction(transactionId);
            return ResponseEntity.ok(transactionService.toDto(transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}