package com.javatodev.finance.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class Statement {
    private UUID id;
    private String accountNumber;
    private BigDecimal amount;
    private String transactionReference;
    private String transactionType; // CREDIT or DEBIT
    private LocalDateTime transactionDate;
    private String sourceService;
    private String description;
    private String createdAt;
}
