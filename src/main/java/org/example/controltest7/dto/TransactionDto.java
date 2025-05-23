package org.example.controltest7.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime dateTime;
    private String status;
}