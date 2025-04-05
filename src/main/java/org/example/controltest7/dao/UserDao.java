package org.example.controltest7.dao;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.model.User;
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
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), username);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public void save(User user) {
        String sql = "INSERT INTO users (username, password, phone_number, enabled) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getPhoneNumber(), user.isEnabled());
    }

    public Long saveAndGetId(User user) {
        String sql = "INSERT INTO users (username, password, phone_number, enabled) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getPhoneNumber());
                    ps.setBoolean(4, user.isEnabled());
                    return ps;
                }, keyHolder
        );
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void saveAuthority(String username, String authority) {
        String sql = "INSERT INTO authorities (username, authority) VALUES (?, ?)";
        jdbcTemplate.update(sql, username, authority);
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}