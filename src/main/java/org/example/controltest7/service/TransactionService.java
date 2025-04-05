package org.example.controltest7.service;

import org.example.controltest7.dto.TransactionCreateDto;
import org.example.controltest7.model.Transaction;

import java.util.List;

public interface TransactionService {
    void createTransaction(String username, Long fromAccountId, TransactionCreateDto transactionDto);

    List<Transaction> getAccountTransactions(String username, Long accountId);

    List<Transaction> getPendingTransactions();

    void approveTransaction(Long transactionId);

    void rejectTransaction(Long transactionId);
}