package com.javatodev.finance.service;

import com.javatodev.finance.model.TransactionStatus;
import com.javatodev.finance.model.dto.UtilityPayment;
import com.javatodev.finance.model.entity.UtilityPaymentEntity;
import com.javatodev.finance.model.mapper.UtilityPaymentMapper;
import com.javatodev.finance.model.rest.request.UtilityPaymentRequest;
import com.javatodev.finance.model.rest.response.UtilityPaymentResponse;
import com.javatodev.finance.repository.UtilityPaymentRepository;
import com.javatodev.finance.service.rest.BankingCoreRestClient;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilityPaymentService {
    private final UtilityPaymentRepository utilityPaymentRepository;
    private final BankingCoreRestClient bankingCoreRestClient;

    private final RabbitTemplate rabbitTemplate;
    private final UtilityPaymentMapper utilityPaymentMapper = new UtilityPaymentMapper();

    // utility payment processing
    public UtilityPaymentResponse utilPayment(UtilityPaymentRequest paymentRequest) {
        log.info("Utility payment processing {}", paymentRequest.toString());
        String transactionId = "";

        UtilityPaymentEntity entity = new UtilityPaymentEntity();
        BeanUtils.copyProperties(paymentRequest, entity);
        entity.setStatus(TransactionStatus.PROCESSING);
        UtilityPaymentEntity optUtilPayment = utilityPaymentRepository.save(entity);

        try {
            // 1. Cut the payment
            UtilityPaymentResponse utilityPaymentResponse = bankingCoreRestClient.utilityPayment(paymentRequest);
            log.info("Transaction response {}", utilityPaymentResponse.toString());
            transactionId = utilityPaymentResponse.getTransactionId();

            // 2. VENDOR DOWN SIMULATION (Money is being cut, but failed to buy ticket)
            boolean isVendorDown = true;
            if (isVendorDown) {
                throw new RuntimeException("Vendor is down");
            }

            optUtilPayment.setStatus(TransactionStatus.SUCCESS);
            optUtilPayment.setTransactionId(utilityPaymentResponse.getTransactionId());
            utilityPaymentRepository.save(optUtilPayment);

            return UtilityPaymentResponse.builder().message("Utility Payment Successfully Processed").transactionId(utilityPaymentResponse.getTransactionId()).build();
        } catch (Exception e) {
            log.error("Utility payment failed", e);
            optUtilPayment.setStatus(TransactionStatus.FAILED);
            optUtilPayment.setTransactionId(transactionId);
            utilityPaymentRepository.save(optUtilPayment);

            // 3. [SAGA CHOREOGRAPHY] If the payment failed, we need to send a message to the queue to roll back the transaction
            try {
                String payload = String.format("{\"transactionId\":\"%s\", \"accountNumber\":\"%s\", \"referenceNumber\":\"%s\", \"amount\":%s}",
                        optUtilPayment.getTransactionId(), paymentRequest.getAccount(), paymentRequest.getReferenceNumber(), paymentRequest.getAmount());

                rabbitTemplate.convertAndSend("utility-payment-events", "payment.failed", payload);
                log.info("Message sent to queue for transaction ID: {}", optUtilPayment.getTransactionId());
            } catch (Exception ex) {
                log.error("Failed to send message to queue", ex);
            }

            throw e;
        }
    }

    public List<UtilityPayment> readPayments(Pageable pageable) {
        Page<UtilityPaymentEntity> allUtilPayments = utilityPaymentRepository.findAll(pageable);
        return utilityPaymentMapper.convertToDtoList(allUtilPayments.getContent());
    }
}
