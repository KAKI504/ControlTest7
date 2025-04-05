package org.example.controltest7.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.dao.TransactionDao;
import org.example.controltest7.dto.TransactionDto;
import org.example.controltest7.model.Account;
import org.example.controltest7.model.Transaction;
import org.example.controltest7.service.AccountService;
import org.example.controltest7.service.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final AccountService accountService;

    @Override
    public Transaction createTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Сумма транзакции должна быть положительной");
        }

        Account fromAccount = accountService.findById(fromAccountId);
        Account toAccount = accountService.findById(toAccountId);

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            throw new RuntimeException("Транзакции возможны только между счетами с одинаковой валютой");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Недостаточно средств на счете");
        }

        Transaction transaction = new Transaction();
        transaction.setFromAccountId(fromAccountId);
        transaction.setToAccountId(toAccountId);
        transaction.setAmount(amount);
        transaction.setCurrency(fromAccount.getCurrency());
        transaction.setDateTime(LocalDateTime.now());
        transaction.setStatus("PENDING");

        Long transactionId = transactionDao.createTransaction(transaction);
        transaction.setId(transactionId);

        return transaction;
    }

    @Override
    public Transaction findById(Long transactionId) {
        return transactionDao.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Транзакция не найдена"));
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        return transactionDao.findByAccountId(accountId);
    }

    @Override
    public List<Transaction> findPendingTransactions() {
        return transactionDao.findPendingTransactions();
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return transactionDao.findAllTransactions();
    }

    @Override
    public Transaction approveTransaction(Long transactionId) {
        Transaction transaction = findById(transactionId);

        if (!"PENDING".equals(transaction.getStatus())) {
            throw new RuntimeException("Можно подтвердить только транзакции в статусе PENDING");
        }

        Account fromAccount = accountService.findById(transaction.getFromAccountId());
        Account toAccount = accountService.findById(transaction.getToAccountId());

        BigDecimal newFromBalance = fromAccount.getBalance().subtract(transaction.getAmount());
        BigDecimal newToBalance = toAccount.getBalance().add(transaction.getAmount());

        accountService.updateBalance(fromAccount.getId(), newFromBalance);
        accountService.updateBalance(toAccount.getId(), newToBalance);

        transactionDao.updateStatus(transactionId, "APPROVED");
        transaction.setStatus("APPROVED");

        return transaction;
    }

    @Override
    public Transaction rejectTransaction(Long transactionId) {
        Transaction transaction = findById(transactionId);

        if (!"PENDING".equals(transaction.getStatus())) {
            throw new RuntimeException("Можно отклонить только транзакции в статусе PENDING");
        }

        transactionDao.updateStatus(transactionId, "REJECTED");
        transaction.setStatus("REJECTED");

        return transaction;
    }

    @Override
    public TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setFromAccountId(transaction.getFromAccountId());
        dto.setToAccountId(transaction.getToAccountId());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setDateTime(transaction.getDateTime());
        dto.setStatus(transaction.getStatus());
        return dto;
    }
}