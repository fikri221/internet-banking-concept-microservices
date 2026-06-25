package com.javatodev.finance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatodev.finance.model.entity.StatementEntity;
import com.javatodev.finance.model.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class StatementMessageListener {
    private final ObjectMapper objectMapper;
    private final StatementRepository statementRepository;

    /**
     * Queue Binding for Fund Transfer Success Event
     * 1. Queue name is statement-fund-transfer-queue created by the receiver
     * 2. Exchange name is fund-transfer-events created by the sender (post office name)
     * 3. The key is TRANSFER_SUCCESS (label)
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "statement-fund-transfer-queue", durable = "true"),
            exchange = @Exchange(value = "fund-transfer-events", type = "topic"), key = "TRANSFER_SUCCESS"))
    @Transactional
    public void handleTransferSuccess(String payload) {
        log.info("Received message: {}", payload);

        try {
            // 1. Read the JSON payload and use JsonNode to access the data
            JsonNode jsonNode = objectMapper.readTree(payload);

            String transactionReference = jsonNode.get("transactionReference").asText();
            String fromAccount = jsonNode.get("fromAccount").asText();
            String toAccount = jsonNode.get("toAccount").asText();
            BigDecimal amount = new BigDecimal(jsonNode.get("amount").asText());

            // 2. Create DEBIT mutation (Money outs) for the SENDER
            StatementEntity debitStatement = new StatementEntity();
            debitStatement.setAccountNumber(fromAccount);
            debitStatement.setTransactionType("DEBIT");
            debitStatement.setTransactionReference(transactionReference);
            debitStatement.setTransactionDate(LocalDateTime.now());
            debitStatement.setAmount(amount);
            debitStatement.setSourceService("FUND_TRANSFER");
            debitStatement.setDescription("Transfer to " + toAccount);

            // 3. Create CREDIT mutation (Money ins) for the RECEIVER
            StatementEntity creditStatement = new StatementEntity();
            creditStatement.setAccountNumber(toAccount);
            creditStatement.setTransactionType("CREDIT");
            creditStatement.setTransactionReference(transactionReference);
            creditStatement.setTransactionDate(LocalDateTime.now());
            creditStatement.setAmount(amount);
            creditStatement.setSourceService("FUND_TRANSFER");
            creditStatement.setDescription("Transfer from " + fromAccount);

            // 4. Save both statements
            statementRepository.save(debitStatement);
            statementRepository.save(creditStatement);

            log.info("Statement saved successfully for transaction ID: {}", transactionReference);
        } catch (Exception e) {
            log.error("Error processing RabbitMQ message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process event", e);
        }
    }
}
