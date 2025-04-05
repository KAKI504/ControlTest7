package org.example.controltest7.dao;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.model.Transaction;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionDao {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public Long createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (from_account_id, to_account_id, amount, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
                    ps.setLong(1, transaction.getFromAccountId());
                    ps.setLong(2, transaction.getToAccountId());
                    ps.setBigDecimal(3, transaction.getAmount());
                    ps.setString(4, transaction.getStatus());
                    ps.setObject(5, transaction.getCreatedAt());
                    return ps;
                },
                keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Transaction> findById(Long id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class), id)));
    }

    public List<Transaction> findByAccountId(Long accountId) {
        String sql = "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class), accountId, accountId);
    }

    public List<Transaction> findPendingTransactions() {
        String sql = "SELECT * FROM transactions WHERE status = 'PENDING' ORDER BY created_at";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class));
    }

    public List<Transaction> findAllTransactions() {
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class));
    }

    public void updateStatus(Long id, String status) {
        String sql = "UPDATE transactions SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }
}