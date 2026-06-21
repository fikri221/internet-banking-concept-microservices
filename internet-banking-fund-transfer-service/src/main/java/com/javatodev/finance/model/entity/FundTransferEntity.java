package com.javatodev.finance.model.entity;

import com.javatodev.finance.model.TransactionStatus;
import com.javatodev.finance.model.dto.AuditAware;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fund_transfer")
public class FundTransferEntity extends AuditAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionReference;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
