package com.javatodev.finance.model.entity;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;

@Getter
@Setter
@Entity
@Table(name = "transaction_statement")
public class StatementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String accountNumber;
    private BigDecimal amount;
    private String transactionReference;
    private String transactionType; // CREDIT or DEBIT
    private LocalDateTime transactionDate;
    private String sourceService;
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
