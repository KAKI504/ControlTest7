package org.example.controltest7.service;

import org.example.controltest7.dto.AccountDto;
import org.example.controltest7.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(Long userId, String currency);
    Account findById(Long accountId);
    List<Account> findByUserId(Long userId);
    void updateBalance(Long accountId, BigDecimal newBalance);
    Account deposit(Long accountId, BigDecimal amount);
    AccountDto toDto(Account account);
}