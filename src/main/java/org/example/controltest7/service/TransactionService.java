package org.example.controltest7.service;

import org.example.controltest7.dto.TransactionDto;
import org.example.controltest7.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount);
    Transaction findById(Long transactionId);
    List<Transaction> findByAccountId(Long accountId);
    List<Transaction> findPendingTransactions();
    List<Transaction> findAllTransactions();
    Transaction approveTransaction(Long transactionId);
    Transaction rejectTransaction(Long transactionId);
    TransactionDto toDto(Transaction transaction);
}