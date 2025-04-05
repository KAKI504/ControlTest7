package org.example.controltest7.dao;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.model.Account;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public void create(Account account) {
        String sql = "INSERT INTO accounts (user_id, currency, balance) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, account.getUserId(), account.getCurrency(), account.getBalance());
    }

    public Optional<Account> findById(Long id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class), id)));
    }

    public List<Account> findByUserId(Long userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class), userId);
    }

    public Optional<Account> findByUserIdAndCurrency(Long userId, String currency) {
        String sql = "SELECT * FROM accounts WHERE user_id = ? AND currency = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class), userId, currency)));
    }

    public void updateBalance(Long id, java.math.BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, id);
    }
}