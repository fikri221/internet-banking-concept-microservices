package com.javatodev.finance.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outbox_event")
public class OutboxEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String aggregateType; // e.g = "FUND_TRANSFER""
    private String aggregateId; // e.g = "Transaction ID"
    private String type; // e.g = "TRANSFER_SUCCESS"
    private String destination; // e.g. = "exchange-name" or "queue-name"

    @Column(columnDefinition = "TEXT")
    private String payload; // Data transaction in JSON format
    private String status; // e.g. = "PENDING" or "SENT"
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
