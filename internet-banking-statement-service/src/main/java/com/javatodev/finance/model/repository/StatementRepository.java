package com.javatodev.finance.model.repository;

import com.javatodev.finance.model.entity.StatementEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StatementRepository extends JpaRepository<StatementEntity, UUID> {

    Page<StatementEntity> findByAccountNumberOrderByTransactionDateDesc(String accountNumber, Pageable pageable);
    Boolean existsByTransactionReferenceAndAccountNumber(String transactionReference, String accountNumber);
}
