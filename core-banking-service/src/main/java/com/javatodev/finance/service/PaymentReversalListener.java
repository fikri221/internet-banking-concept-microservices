package com.javatodev.finance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatodev.finance.model.dto.request.UtilityPaymentRequest;
import com.javatodev.finance.model.dto.response.UtilityPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class PaymentReversalListener {
    private final ObjectMapper objectMapper;
    private final TransactionService transactionService;

    /**
     * Queue Binding for Payment Reversal Event
     * 1. Queue name is payment-reversal-events created by the receiver
     * 2. Exchange name is utility-payment-events created by the sender (post office name)
     * 3. The key is payment.failed (label)
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "payment-reversal-events", durable = "true"),
    exchange = @Exchange(value = "utility-payment-events", type = "topic"), key = "payment.failed"))
    @Transactional
    public void handlePaymentReversalEvent(String payload) {
        log.info("Received payment reversal payload: {}", payload);

        try {
            // 1. Read the JSON payload and use JsonNode to access the data
            JsonNode jsonNode = objectMapper.readTree(payload);

            String transactionId = jsonNode.get("transactionId").asText();
            String referenceNumber = jsonNode.get("referenceNumber").asText();
            String accountNumber = jsonNode.get("accountNumber").asText();
            BigDecimal amount = new BigDecimal(jsonNode.get("amount").asText());

            // 2. Process the payment reversal event
            UtilityPaymentRequest req = new UtilityPaymentRequest();
            req.setReferenceNumber(referenceNumber);
            req.setAccount(accountNumber);
            req.setAmount(amount);

            // 3. Call the transaction service to process the payment reversal
            UtilityPaymentResponse res = transactionService.reversalUtilityPayment(req, transactionId);
            log.info("Payment reversal processed successfully for transaction ID: {}, Message: {}", transactionId,
                    res.getMessage());
        } catch (Exception e) {
            log.error("Error processing payment reversal event: {}", e.toString());
            throw new RuntimeException("Reversal failed, triggering retry", e);
        }
    }
}
