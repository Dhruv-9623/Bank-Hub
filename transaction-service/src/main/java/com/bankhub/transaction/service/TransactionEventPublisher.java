package com.bankhub.transaction.service;

import com.bankhub.transaction.entity.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishTransactionEvent(Transaction transaction) {
        try {
            // Create transaction event data
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("transactionId", transaction.getTransactionId());
            eventData.put("userId", transaction.getUserId());
            eventData.put("fromAccount", transaction.getFromAccount());
            eventData.put("toAccount", transaction.getToAccount());
            eventData.put("amount", transaction.getAmount());
            eventData.put("transactionType", transaction.getTransactionType());
            eventData.put("status", transaction.getStatus());
            eventData.put("description", transaction.getDescription());
            eventData.put("timestamp", transaction.getCreatedAt());

            // Publish to Kafka topic
            kafkaTemplate.send("transaction-events", transaction.getTransactionId(), eventData);

            log.info("🚀 Published transaction event to Kafka: {}", transaction.getTransactionId());

        } catch (Exception e) {
            log.error("❌ Failed to publish transaction event: {} - Error: {}",
                    transaction.getTransactionId(), e.getMessage());
        }
    }
}