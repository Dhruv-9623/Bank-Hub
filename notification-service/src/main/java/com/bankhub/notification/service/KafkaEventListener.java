package com.bankhub.notification.service;

import com.bankhub.notification.dto.FraudEventDto;
import com.bankhub.notification.dto.NotificationRequestDto;
import com.bankhub.notification.dto.TransactionEventDto;
import com.bankhub.notification.entity.NotificationChannel;
import com.bankhub.notification.entity.NotificationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "transaction-events", groupId = "notification-service")
    public void handleTransactionEvent(String message) {
        try {
            log.info("Received transaction event: {}", message);

            TransactionEventDto transactionEvent = objectMapper.readValue(message, TransactionEventDto.class);

            // Create transaction notification
            NotificationRequestDto notification = NotificationRequestDto.builder()
                    .userId(transactionEvent.getUserId())
                    .recipientEmail("user" + transactionEvent.getUserId() + "@bankhub-demo.com")// In real app, fetch from user service
                    .notificationType(NotificationType.TRANSACTION_ALERT)
                    .channel(NotificationChannel.EMAIL)
                    .subject("Transaction Alert - BankHub")
                    .message(createTransactionMessage(transactionEvent))
                    .sourceEvent("transaction-completed")
                    .eventData(message)
                    .build();

            notificationService.sendNotification(notification);

        } catch (Exception e) {
            log.error("Error processing transaction event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "fraud-events", groupId = "notification-service")
    public void handleFraudEvent(String message) {
        try {
            log.info("Received fraud event: {}", message);

            FraudEventDto fraudEvent = objectMapper.readValue(message, FraudEventDto.class);

            // Create fraud alert notification
            NotificationRequestDto notification = NotificationRequestDto.builder()
                    .userId(fraudEvent.getUserId())
                    .recipientEmail("user" + fraudEvent.getUserId() + "@bankhub-demo.com")
                    .notificationType(NotificationType.FRAUD_ALERT)
                    .channel(NotificationChannel.EMAIL)
                    .subject("🚨 FRAUD ALERT - Immediate Action Required")
                    .message(createFraudMessage(fraudEvent))
                    .sourceEvent("fraud-detected")
                    .eventData(message)
                    .build();

            notificationService.sendNotification(notification);

            // Also send SMS for high-risk transactions
            if ("HIGH".equals(fraudEvent.getRiskLevel()) || "CRITICAL".equals(fraudEvent.getRiskLevel())) {
                NotificationRequestDto smsNotification = NotificationRequestDto.builder()
                        .userId(fraudEvent.getUserId())
                        .recipientEmail(fraudEvent.getUserId() + "@bankhub.com")
                        .recipientPhone("+1-555-0" + fraudEvent.getUserId() + "01") // In real app, fetch from user service
                        .notificationType(NotificationType.FRAUD_ALERT)
                        .channel(NotificationChannel.SMS)
                        .subject("🚨 FRAUD ALERT - Immediate Action Required")
                        .message(createFraudSmsMessage(fraudEvent))
                        .sourceEvent("fraud-detected")
                        .eventData(message)
                        .build();

                notificationService.sendNotification(smsNotification);
            }

        } catch (Exception e) {
            log.error("Error processing fraud event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "account-events", groupId = "notification-service")
    public void handleAccountEvent(String message) {
        try {
            log.info("Received account event: {}", message);

            // Handle account creation, balance updates, etc.
            // Implementation similar to above

        } catch (Exception e) {
            log.error("Error processing account event: {}", e.getMessage(), e);
        }
    }

    private String createTransactionMessage(TransactionEventDto event) {
        return String.format(
                "Dear Customer,\n\n" +
                        "A transaction has been processed on your account:\n\n" +
                        "Transaction ID: %s\n" +
                        "Type: %s\n" +
                        "Amount: $%.2f\n" +
                        "From Account: %s\n" +
                        "To Account: %s\n" +
                        "Description: %s\n" +
                        "Status: %s\n" +
                        "Date: %s\n\n" +
                        "If you did not authorize this transaction, please contact us immediately.\n\n" +
                        "Best regards,\n" +
                        "BankHub Security Team",
                event.getTransactionId(),
                event.getTransactionType(),
                event.getAmount(),
                event.getFromAccount(),
                event.getToAccount(),
                event.getDescription(),
                event.getStatus(),
                event.getTimestamp()
        );
    }

    private String createFraudMessage(FraudEventDto event) {
        return String.format(
                "🚨 FRAUD ALERT 🚨\n\n" +
                        "Dear Customer,\n\n" +
                        "Our AI fraud detection system has identified suspicious activity on your account:\n\n" +
                        "Transaction ID: %s\n" +
                        "Account: %s\n" +
                        "Amount: $%.2f\n" +
                        "Risk Level: %s\n" +
                        "Risk Score: %.0f%%\n" +
                        "Decision: %s\n" +
                        "Fraud Indicators: %s\n" +
                        "Detected At: %s\n\n" +
                        "IMMEDIATE ACTION REQUIRED:\n" +
                        "1. If this was you, please ignore this alert\n" +
                        "2. If this was NOT you, call us immediately at 1-800-BANKHUB\n" +
                        "3. Consider changing your account passwords\n\n" +
                        "Your account security is our top priority.\n\n" +
                        "BankHub Fraud Prevention Team",
                event.getTransactionId(),
                event.getAccountNumber(),
                event.getAmount(),
                event.getRiskLevel(),
                event.getRiskScore(),
                event.getDecision(),
                event.getFraudReasons(),
                event.getTimestamp()
        );
    }

    private String createFraudSmsMessage(FraudEventDto event) {
        return String.format(
                "🚨 BANKHUB FRAUD ALERT: Suspicious activity detected on account %s. Amount: $%.2f. Risk: %s. Call 1-800-BANKHUB immediately if not authorized.",
                event.getAccountNumber().substring(event.getAccountNumber().length() - 4),
                event.getAmount(),
                event.getRiskLevel()
        );
    }
}