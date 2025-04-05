package org.example.controltest7.controller;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.model.Transaction;
import org.example.controltest7.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final TransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getPendingTransactions() {
        List<Transaction> transactions = transactionService.getPendingTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transactions/{transactionId}/approve")
    public ResponseEntity<String> approveTransaction(@PathVariable Long transactionId) {
        transactionService.approveTransaction(transactionId);
        return ResponseEntity.ok("Transaction approved successfully");
    }

    @PostMapping("/transactions/{transactionId}/reject")
    public ResponseEntity<String> rejectTransaction(@PathVariable Long transactionId) {
        transactionService.rejectTransaction(transactionId);
        return ResponseEntity.ok("Transaction rejected successfully");
    }
}