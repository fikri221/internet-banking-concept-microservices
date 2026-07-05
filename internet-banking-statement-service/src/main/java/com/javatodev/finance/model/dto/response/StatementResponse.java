package com.javatodev.finance.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatementResponse {
    private UUID id;
    private String accountNumber;
    private BigDecimal amount;
    private String transactionReference;
    private String transactionType; // CREDIT or DEBIT
    private LocalDateTime transactionDate;
    private String sourceService;
    private String description;
    private LocalDateTime createdAt;
    private String message;
}
