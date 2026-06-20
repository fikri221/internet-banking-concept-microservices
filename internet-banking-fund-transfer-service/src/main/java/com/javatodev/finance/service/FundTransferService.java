package com.javatodev.finance.service;

import com.javatodev.finance.model.TransactionStatus;
import com.javatodev.finance.model.dto.FundTransfer;
import com.javatodev.finance.model.dto.request.FundTransferRequest;
import com.javatodev.finance.model.dto.response.FundTransferResponse;
import com.javatodev.finance.model.entity.FundTransferEntity;
import com.javatodev.finance.model.mapper.FundTransferMapper;
import com.javatodev.finance.model.repository.FundTransferRepository;
import com.javatodev.finance.service.rest.client.BankingCoreFeignClient;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FundTransferService {

    private final FundTransferRepository fundTransferRepository;
    private final BankingCoreFeignClient bankingCoreFeignClient;

    private final FundTransferMapper mapper = new FundTransferMapper();

    public FundTransferResponse fundTransfer(FundTransferRequest request, String idempotencyKey) {
        log.info("Sending fund transfer request {}", request.toString());

        Optional<FundTransferEntity> optionalKey = fundTransferRepository.findByIdempotencyKey(idempotencyKey);
        String tempIdempotencyKey;
        if (optionalKey.isPresent()) {
            throw new RuntimeException("Idempotency Key already exists");
        } else {
            tempIdempotencyKey = idempotencyKey;
        }

        FundTransferEntity entity = new FundTransferEntity();
        BeanUtils.copyProperties(request, entity);
        entity.setStatus(TransactionStatus.PENDING);
        entity.setIdempotencyKey(tempIdempotencyKey);
        FundTransferEntity optFundTransfer = fundTransferRepository.save(entity);

        try {
            FundTransferResponse fundTransferResponse = bankingCoreFeignClient.fundTransfer(request);
            optFundTransfer.setTransactionReference(fundTransferResponse.getTransactionId());
            optFundTransfer.setStatus(TransactionStatus.SUCCESS);
            fundTransferRepository.save(optFundTransfer);

            fundTransferResponse.setMessage("Fund Transfer Successfully Completed");
            return fundTransferResponse;
        } catch (Exception e) {
            log.error("Fund transfer failed", e);
            optFundTransfer.setStatus(TransactionStatus.FAILED);
            fundTransferRepository.save(optFundTransfer);
            throw e;
        }
    }

    public List<FundTransfer> readAllTransfers(Pageable pageable) {
        return mapper.convertToDtoList(fundTransferRepository.findAll(pageable).getContent());
    }
}
