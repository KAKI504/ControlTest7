package org.example.controltest7.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private Long id;
    private Long userId;
    private String currency;
    private BigDecimal balance;
}