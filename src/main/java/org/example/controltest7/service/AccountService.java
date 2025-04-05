package org.example.controltest7.service;

import org.example.controltest7.dto.AccountCreateDto;
import org.example.controltest7.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    void createAccount(String username, AccountCreateDto accountCreateDto);

    List<Account> getUserAccounts(String username);

    BigDecimal getAccountBalance(String username, Long accountId);

    void depositFunds(String username, Long accountId, BigDecimal amount);
}