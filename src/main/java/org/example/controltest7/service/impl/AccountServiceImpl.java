package org.example.controltest7.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controltest7.dao.AccountDao;
import org.example.controltest7.dao.UserDao;
import org.example.controltest7.dto.AccountCreateDto;
import org.example.controltest7.exception.AccountAlreadyExistsException;
import org.example.controltest7.exception.AccountNotFoundException;
import org.example.controltest7.exception.UserNotFoundException;
import org.example.controltest7.model.Account;
import org.example.controltest7.model.User;
import org.example.controltest7.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;
    private final UserDao userDao;

    @Override
    @Transactional
    public void createAccount(String username, AccountCreateDto accountCreateDto) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        if (accountDao.findByUserIdAndCurrency(user.getId(), accountCreateDto.getCurrency()).isPresent()) {
            throw new AccountAlreadyExistsException("Account with this currency already exists");
        }

        Account account = Account.builder()
                .userId(user.getId())
                .currency(accountCreateDto.getCurrency())
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();

        accountDao.create(account);
    }

    @Override
    public List<Account> getUserAccounts(String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return accountDao.findByUserId(user.getId());
    }

    @Override
    public BigDecimal getAccountBalance(String username, Long accountId) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        Account account = accountDao.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        log.info("User ID from DB: {}, Account User ID: {}", user.getId(), account.getUserId());

        if (!account.getUserId().equals(user.getId())) {
            log.warn("Account {} belongs to user ID {} but accessed by user ID {}",
                    accountId, account.getUserId(), user.getId());
            throw new RuntimeException("Account does not belong to user");
        }

        return account.getBalance();
    }


    @Override
    @Transactional
    public void depositFunds(String username, Long accountId, BigDecimal amount) {
        log.info("Depositing {} to account {} for user {}", amount, accountId, username);

        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        Account account = accountDao.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        if (!account.getUserId().equals(user.getId())) {
            log.warn("User {} trying to access account that doesn't belong to them", username);
            throw new RuntimeException("Account does not belong to user");
        }

        BigDecimal newBalance = account.getBalance().add(amount);
        log.info("Updating balance from {} to {}", account.getBalance(), newBalance);

        accountDao.updateBalance(accountId, newBalance);
        log.info("Funds deposited successfully");
    }


}