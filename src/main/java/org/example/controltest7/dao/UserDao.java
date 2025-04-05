package org.example.controltest7.dao;

import lombok.RequiredArgsConstructor;
import org.example.controltest7.model.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username)));
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM users WHERE phone_number = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), phoneNumber)));
    }

    public void create(User user) {
        String sql = "INSERT INTO users (username, phone_number, password, enabled) " +
                "VALUES (:username, :phoneNumber, :password, :enabled)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("phoneNumber", user.getPhoneNumber())
                .addValue("password", user.getPassword())
                .addValue("enabled", user.isEnabled());

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void addUserAuthority(String username, String authority) {
        String sql = "INSERT INTO authorities (username, authority) VALUES (?, ?)";
        jdbcTemplate.update(sql, username, authority);
    }

    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public void blockUser(String username, boolean blocked) {
        String sql = "UPDATE users SET blocked = ? WHERE username = ?";
        jdbcTemplate.update(sql, blocked, username);
    }
}