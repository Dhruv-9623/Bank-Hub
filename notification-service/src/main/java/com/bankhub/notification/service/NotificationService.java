package com.bankhub.notification.service;

import com.bankhub.notification.dto.NotificationRequestDto;
import com.bankhub.notification.entity.NotificationChannel;
import com.bankhub.notification.entity.NotificationEvent;
import com.bankhub.notification.entity.NotificationStatus;
import com.bankhub.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    public void sendNotification(NotificationRequestDto request) {
        String eventId = UUID.randomUUID().toString();

        log.info("Processing notification request: {} for user: {}",
                request.getNotificationType(), request.getUserId());

        // Create notification event record
        NotificationEvent event = NotificationEvent.builder()
                .eventId(eventId)
                .userId(request.getUserId())
                .recipientEmail(request.getRecipientEmail())
                .recipientPhone(request.getRecipientPhone())
                .notificationType(request.getNotificationType())
                .channel(request.getChannel())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(NotificationStatus.PENDING)
                .sourceEvent(request.getSourceEvent())
                .eventData(request.getEventData())
                .build();

        NotificationEvent savedEvent = notificationRepository.save(event);

        try {
            // Send notification based on channel
            switch (request.getChannel()) {
                case EMAIL:
                    sendEmailNotification(savedEvent);
                    break;
                case SMS:
                    sendSmsNotification(savedEvent);
                    break;
                case PUSH:
                    sendPushNotification(savedEvent);
                    break;
                case IN_APP:
                    sendInAppNotification(savedEvent);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported notification channel: " + request.getChannel());
            }

            // Update status to sent
            savedEvent.setStatus(NotificationStatus.SENT);
            savedEvent.setSentAt(LocalDateTime.now());
            notificationRepository.save(savedEvent);

            log.info("Notification sent successfully: {} via {}", eventId, request.getChannel());

        } catch (Exception e) {
            // Update status to failed
            savedEvent.setStatus(NotificationStatus.FAILED);
            savedEvent.setErrorMessage(e.getMessage());
            notificationRepository.save(savedEvent);

            log.error("Failed to send notification: {} - Error: {}", eventId, e.getMessage());
            throw new RuntimeException("Notification sending failed", e);
        }
    }

    private void sendEmailNotification(NotificationEvent event) {
        try {
            // ✅ SIMULATE EMAIL FOR DEMO - Beautiful console display
            log.info("📧 =============== EMAIL NOTIFICATION SENT ===============");
            log.info("📧 TO: {}", event.getRecipientEmail());
            log.info("📧 SUBJECT: {}", event.getSubject());
            log.info("📧 MESSAGE:");
            log.info("📧 {}", event.getMessage());
            log.info("📧 ====================================================");
            log.info("✅ Email notification sent successfully (simulated)!");

            // Simulate email processing time
            Thread.sleep(50);

        } catch (Exception e) {
            log.error("Failed to send email to: {} - Error: {}", event.getRecipientEmail(), e.getMessage());
            throw new RuntimeException("Email simulation failed", e);
        }
    }

    private void sendSmsNotification(NotificationEvent event) {
        // In a real application, integrate with SMS provider like Twilio
        log.info("SMS notification simulated for: {} - Message: {}",
                event.getRecipientPhone(), event.getMessage());

        // Simulate SMS sending delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendPushNotification(NotificationEvent event) {
        // In a real application, integrate with push notification service
        log.info("Push notification simulated for user: {} - Message: {}",
                event.getUserId(), event.getMessage());
    }

    private void sendInAppNotification(NotificationEvent event) {
        // In a real application, store in database for in-app display
        log.info("In-app notification created for user: {} - Message: {}",
                event.getUserId(), event.getMessage());
    }

    public List<NotificationEvent> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<NotificationEvent> getFailedNotifications() {
        return notificationRepository.findByStatus(NotificationStatus.FAILED);
    }

    public long getSentNotificationsCount(LocalDateTime since) {
        return notificationRepository.countSentNotificationsSince(since);
    }

    public long getFailedNotificationsCount(LocalDateTime since) {
        return notificationRepository.countFailedNotificationsSince(since);
    }

    public void retryFailedNotifications() {
        List<NotificationEvent> failedNotifications = getFailedNotifications();

        for (NotificationEvent event : failedNotifications) {
            try {
                NotificationRequestDto retryRequest = NotificationRequestDto.builder()
                        .userId(event.getUserId())
                        .recipientEmail(event.getRecipientEmail())
                        .recipientPhone(event.getRecipientPhone())
                        .notificationType(event.getNotificationType())
                        .channel(event.getChannel())
                        .subject(event.getSubject())
                        .message(event.getMessage())
                        .sourceEvent(event.getSourceEvent())
                        .eventData(event.getEventData())
                        .build();

                sendNotification(retryRequest);

                // Mark original as retry
                event.setStatus(NotificationStatus.RETRY);
                notificationRepository.save(event);

            } catch (Exception e) {
                log.error("Retry failed for notification: {} - Error: {}", event.getEventId(), e.getMessage());
            }
        }
    }
}