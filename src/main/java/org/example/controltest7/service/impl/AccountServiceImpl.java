package org.example.controltest7.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.dao.AccountDao;
import org.example.controltest7.dto.AccountDto;
import org.example.controltest7.model.Account;
import org.example.controltest7.service.AccountService;
import org.example.controltest7.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final UserService userService;

    @Override
    public Account createAccount(Long userId, String currency) {
        userService.findById(userId);

        int accountCount = accountDao.countAccountsByUserId(userId);
        if (accountCount >= 3) {
            throw new RuntimeException("Пользователь не может иметь более 3 счетов");
        }

        Account account = new Account();
        account.setUserId(userId);
        account.setCurrency(currency);
        account.setBalance(BigDecimal.ZERO);

        Long accountId = accountDao.createAccount(account);
        account.setId(accountId);

        return account;
    }

    @Override
    public Account findById(Long accountId) {
        return accountDao.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Счет не найден"));
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        return accountDao.findByUserId(userId);
    }

    @Override
    public void updateBalance(Long accountId, BigDecimal newBalance) {
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Баланс не может быть отрицательным");
        }
        accountDao.updateBalance(accountId, newBalance);
    }

    @Override
    public Account deposit(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Сумма пополнения должна быть положительной");
        }

        Account account = findById(accountId);
        BigDecimal newBalance = account.getBalance().add(amount);
        updateBalance(accountId, newBalance);
        account.setBalance(newBalance);

        return account;
    }

    @Override
    public AccountDto toDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setCurrency(account.getCurrency());
        dto.setBalance(account.getBalance());
        return dto;
    }
}