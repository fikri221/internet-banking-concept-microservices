package com.javatodev.finance.model.repository;

import com.javatodev.finance.model.entity.OutboxEventEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OutboxEventEntity> findByStatus(String status, Pageable pageable);
}
