package org.example.controltest7.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private Long id;
    private String currency;
    private BigDecimal balance;
}