package org.example.controltest7.dao;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.model.Account;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountDao {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public Long createAccount(Account account) {
        String sql = "INSERT INTO accounts (user_id, currency, balance) VALUES (?, ?, ?)";

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    ps.setLong(1, account.getUserId());
                    ps.setString(2, account.getCurrency());
                    ps.setBigDecimal(3, account.getBalance());
                    return ps;
                }, keyHolder
        );
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Account> findById(Long id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class), id)
                )
        );
    }

    public List<Account> findByUserId(Long userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class), userId);
    }

    public void updateBalance(Long accountId, java.math.BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, accountId);
    }

    public int countAccountsByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }
}