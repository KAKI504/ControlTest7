package org.example.controltest7.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controltest7.dao.TransactionDao;
import org.example.controltest7.dao.AccountDao;
import org.example.controltest7.dao.UserDao;
import org.example.controltest7.dto.TransactionCreateDto;
import org.example.controltest7.exception.AccountNotFoundException;
import org.example.controltest7.exception.TransactionNotFoundException;
import org.example.controltest7.exception.UserNotFoundException;
import org.example.controltest7.model.Account;
import org.example.controltest7.model.Transaction;
import org.example.controltest7.model.User;
import org.example.controltest7.service.CurrencyConversionService;
import org.example.controltest7.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionDao transactionDao;
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final CurrencyConversionService currencyConversionService;

    @Override
    @Transactional
    public void createTransaction(String username, Long fromAccountId, TransactionCreateDto transactionDto) {
        log.info("Creating transaction from account {} to account {} for user {}",
                fromAccountId, transactionDto.getToAccountId(), username);

        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        log.info("User found: id={}, username={}", user.getId(), user.getUsername());

        Account fromAccount = accountDao.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Source account not found: " + fromAccountId));
        log.info("Source account found: id={}, userId={}, currency={}",
                fromAccount.getId(), fromAccount.getUserId(), fromAccount.getCurrency());

        try {
            Account toAccount = accountDao.findById(transactionDto.getToAccountId())
                    .orElseThrow(() -> new AccountNotFoundException("Target account not found: " + transactionDto.getToAccountId()));
            log.info("Target account found: id={}, userId={}, currency={}",
                    toAccount.getId(), toAccount.getUserId(), toAccount.getCurrency());

            if (!fromAccount.getUserId().equals(user.getId())) {
                log.warn("User {} trying to access account that doesn't belong to them", username);
                throw new RuntimeException("Source account does not belong to user");
            }

            BigDecimal amount = transactionDto.getAmount();
            if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
                log.info("Multicurrency transaction from {} to {}",
                        fromAccount.getCurrency(), toAccount.getCurrency());
            }

            Transaction transaction = Transaction.builder()
                    .fromAccountId(fromAccountId)
                    .toAccountId(transactionDto.getToAccountId())
                    .amount(amount)
                    .status("PENDING")
                    .createdAt(LocalDateTime.now())
                    .build();

            transactionDao.createTransaction(transaction);
            log.info("Transaction created successfully");
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void approveTransaction(Long transactionId) {
        log.info("Approving transaction {}", transactionId);

        Transaction transaction = transactionDao.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + transactionId));

        if (!"PENDING".equals(transaction.getStatus())) {
            log.warn("Cannot approve transaction {} with status {}", transactionId, transaction.getStatus());
            throw new RuntimeException("Transaction is not in PENDING status");
        }

        Account fromAccount = accountDao.findById(transaction.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));
        Account toAccount = accountDao.findById(transaction.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Target account not found"));

        if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            log.warn("Insufficient funds for transaction {}", transactionId);
            transactionDao.updateStatus(transactionId, "REJECTED");
            throw new RuntimeException("Insufficient funds");
        }

        BigDecimal debitAmount = transaction.getAmount();
        BigDecimal creditAmount = transaction.getAmount();

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            creditAmount = currencyConversionService.convert(
                    debitAmount,
                    fromAccount.getCurrency(),
                    toAccount.getCurrency());

            log.info("Converted {} {} to {} {}",
                    debitAmount, fromAccount.getCurrency(),
                    creditAmount, toAccount.getCurrency());
        }

        accountDao.updateBalance(fromAccount.getId(), fromAccount.getBalance().subtract(debitAmount));
        accountDao.updateBalance(toAccount.getId(), toAccount.getBalance().add(creditAmount));

        transactionDao.updateStatus(transactionId, "APPROVED");
        log.info("Transaction {} approved successfully", transactionId);
    }

    @Override
    public List<Transaction> getAccountTransactions(String username, Long accountId) {
        log.info("Getting transactions for account {} for user {}", accountId, username);

        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        Account account = accountDao.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        if (!account.getUserId().equals(user.getId())) {
            log.warn("User {} trying to access account that doesn't belong to them", username);
            throw new RuntimeException("Account does not belong to user");
        }

        List<Transaction> transactions = transactionDao.findByAccountId(accountId);
        log.info("Found {} transactions for account {}", transactions.size(), accountId);
        return transactions;
    }
    @Override
    public List<Transaction> getPendingTransactions() {
        log.info("Getting all pending transactions");
        List<Transaction> transactions = transactionDao.findPendingTransactions();
        log.info("Found {} pending transactions", transactions.size());
        return transactions;
    }

    @Override
    @Transactional
    public void rejectTransaction(Long transactionId) {
        log.info("Rejecting transaction {}", transactionId);

        Transaction transaction = transactionDao.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + transactionId));

        if (!"PENDING".equals(transaction.getStatus())) {
            log.warn("Cannot reject transaction {} with status {}", transactionId, transaction.getStatus());
            throw new RuntimeException("Transaction is not in PENDING status");
        }

        transactionDao.updateStatus(transactionId, "REJECTED");
        log.info("Transaction {} rejected successfully", transactionId);
    }
}