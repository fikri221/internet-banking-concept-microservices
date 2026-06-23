package com.javatodev.finance.service;

import com.javatodev.finance.model.entity.OutboxEventEntity;
import com.javatodev.finance.model.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class OutboxEventPublisher {
    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000) // Run every 5 seconds
    @Transactional
    public void publishPendingEvent() {
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.by("createdAt").ascending());
        // 1. Get all data with pending status
        List<OutboxEventEntity> pendingEvents = outboxEventRepository.findByStatus("PENDING", pageRequest);

        if (pendingEvents.isEmpty()) {
            return;
        }

        for (OutboxEventEntity event : pendingEvents) {
            try {
                // 2. SEND EVENT TO QUEUE SIMULATION (change into RabbitMQ Template later)
//                log.info(">> [RABBITMQ SIMULATION] Mengirim pesan ke broker: {}", event.getPayload());
                rabbitTemplate.convertAndSend(event.getDestination(), event.getPayload());

                // 3. Update the status to SUCCESS if the event is successfully sent
                event.setStatus("SUCCESS");
                outboxEventRepository.save(event);
            } catch (Exception e) {
                // If failed, just let it be PENDING status, so it will be retried 5 seconds later
                log.error("Failed to send event to queue with ID: {}", event.getId(), e);
            }
        }
    }
}
