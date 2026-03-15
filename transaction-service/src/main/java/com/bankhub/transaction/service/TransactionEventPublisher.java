package com.bankhub.transaction.service;

import com.bankhub.transaction.entity.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public TransactionEventPublisher(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

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

            System.out.println("Published transaction event to Kafka: " + transaction.getTransactionId());

        } catch (Exception e) {
            System.err.println("Failed to publish transaction event: " + transaction.getTransactionId() + " - Error: " + e.getMessage());
        }
    }
}