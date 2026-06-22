package com.javatodev.finance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatodev.finance.model.TransactionStatus;
import com.javatodev.finance.model.dto.FundTransfer;
import com.javatodev.finance.model.dto.request.FundTransferRequest;
import com.javatodev.finance.model.dto.response.FundTransferResponse;
import com.javatodev.finance.model.entity.FundTransferEntity;
import com.javatodev.finance.model.entity.OutboxEventEntity;
import com.javatodev.finance.model.mapper.FundTransferMapper;
import com.javatodev.finance.model.repository.FundTransferRepository;
import com.javatodev.finance.model.repository.OutboxEventRepository;
import com.javatodev.finance.service.rest.client.BankingCoreFeignClient;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FundTransferService {

    public static final Long IDEMPOTENCY_KEY_LOCK_TIMEOUT_IN_SECONDS = 10L; // 10 seconds

    private final FundTransferRepository fundTransferRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final BankingCoreFeignClient bankingCoreFeignClient;

    private final FundTransferMapper mapper = new FundTransferMapper();
    private final ObjectMapper objectMapper;

    @Transactional
    public FundTransferResponse fundTransfer(FundTransferRequest request, String idempotencyKey) {
        log.info("Sending fund transfer request {}", request.toString());

        Optional<FundTransferEntity> optionalKey = fundTransferRepository.findByIdempotencyKey(idempotencyKey);
        FundTransferEntity entity = new FundTransferEntity();
        if (optionalKey.isPresent()) {
            if (chekIfIsLocked(optionalKey.get())) {
                throw new IllegalStateException("Idempotency Key is locked");
            }
            if (optionalKey.get().getStatus().equals(TransactionStatus.FAILED)) {
                return FundTransferResponse.builder()
                        .message("Previous transaction failed. Please create a new request.")
                        .build();
            }
            return FundTransferResponse.builder()
                    .transactionId(optionalKey.get().getTransactionReference())
                    .message("Fund Transfer Successfully Completed")
                    .build();
        } else {
            entity.setIdempotencyKey(idempotencyKey);
        }
        BeanUtils.copyProperties(request, entity);
        entity.setStatus(TransactionStatus.PENDING);
        entity.setLockedAt(LocalDateTime.now());
        FundTransferEntity optFundTransfer;

        try {
            // If 2 threads are trying to create the same idempotency key at the same time,
            // the first one will succeed and the second one will throw a DataIntegrityViolationException.
            optFundTransfer = fundTransferRepository.save(entity);
            // Force to flush the entity manager to ensure that the idempotency key is persisted.
            fundTransferRepository.flush();
        } catch (DataIntegrityViolationException e) {
            log.warn("Race condition detected, idempotency key {} currently in use", idempotencyKey);
            throw new IllegalStateException("Request is currently being processed (Race condition intercepted)");
        }

        try {
            FundTransferResponse fundTransferResponse = bankingCoreFeignClient.fundTransfer(request);
            optFundTransfer.setTransactionReference(fundTransferResponse.getTransactionId());
            optFundTransfer.setStatus(TransactionStatus.SUCCESS);
            fundTransferRepository.save(optFundTransfer);

            OutboxEventEntity outboxEvent = new OutboxEventEntity();
            outboxEvent.setAggregateId(optFundTransfer.getTransactionReference());
            outboxEvent.setAggregateType("FUND_TRANSFER");
            outboxEvent.setType("TRANSFER_SUCCESS");
            outboxEvent.setPayload(objectMapper.writeValueAsString(mapper.convertToDto(optFundTransfer)));
            outboxEvent.setStatus("PENDING");
            outboxEventRepository.save(outboxEvent);

            fundTransferResponse.setMessage("Fund Transfer Successfully Completed");
            return fundTransferResponse;
        } catch (Exception e) {
            log.error("Fund transfer failed", e);
            optFundTransfer.setStatus(TransactionStatus.FAILED);
            fundTransferRepository.save(optFundTransfer);
            return  FundTransferResponse.builder().message("Fund Transfer Failed").build();
        }
    }

    /**
     * Check if the idempotency key is locked based on the timeout
     * @param idempotencyKey the idempotency key to check
     * @return true if the idempotency key is locked, false otherwise
     */
    public boolean chekIfIsLocked(FundTransferEntity idempotencyKey) {
        return idempotencyKey.getLockedAt().plusSeconds(IDEMPOTENCY_KEY_LOCK_TIMEOUT_IN_SECONDS).isAfter(LocalDateTime.now());
    }

    /**
     * Read all fund transfers
     * @param pageable Pageable object
     * @return List of fund transfers
     */
    public List<FundTransfer> readAllTransfers(Pageable pageable) {
        return mapper.convertToDtoList(fundTransferRepository.findAll(pageable).getContent());
    }
}
