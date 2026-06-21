package com.javatodev.finance.model.repository;

import com.javatodev.finance.model.entity.FundTransferEntity;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface FundTransferRepository extends JpaRepository<FundTransferEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    Optional<FundTransferEntity> findByIdempotencyKey(String idempotencyKey);
}
