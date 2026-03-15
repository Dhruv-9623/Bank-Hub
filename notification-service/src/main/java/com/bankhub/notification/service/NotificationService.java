package com.bankhub.notification.service;

import com.bankhub.notification.dto.NotificationRequestDto;
import com.bankhub.notification.entity.NotificationChannel;
import com.bankhub.notification.entity.NotificationEvent;
import com.bankhub.notification.entity.NotificationStatus;
import com.bankhub.notification.repository.NotificationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    public NotificationService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    public void sendNotification(NotificationRequestDto request) {
        String eventId = UUID.randomUUID().toString();

        System.out.println("Processing notification request: " + request.getNotificationType() + " for user: " + request.getUserId());

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

            System.out.println("Notification sent successfully: " + eventId + " via " + request.getChannel());

        } catch (Exception e) {
            // Update status to failed
            savedEvent.setStatus(NotificationStatus.FAILED);
            savedEvent.setErrorMessage(e.getMessage());
            notificationRepository.save(savedEvent);

            System.err.println("Failed to send notification: " + eventId + " - Error: " + e.getMessage());
            throw new RuntimeException("Notification sending failed", e);
        }
    }

    private void sendEmailNotification(NotificationEvent event) {
        try {
            // ✅ SIMULATE EMAIL FOR DEMO - Beautiful console display
            System.out.println("📧 =============== EMAIL NOTIFICATION SENT ===============");
            System.out.println("📧 TO: " + event.getRecipientEmail());
            System.out.println("📧 SUBJECT: " + event.getSubject());
            System.out.println("📧 MESSAGE:");
            System.out.println("📧 " + event.getMessage());
            System.out.println("📧 ====================================================");
            System.out.println("✅ Email notification sent successfully (simulated)!");

            // Simulate email processing time
            Thread.sleep(50);

        } catch (Exception e) {
            System.err.println("Failed to send email to: " + event.getRecipientEmail() + " - Error: " + e.getMessage());
            throw new RuntimeException("Email simulation failed", e);
        }
    }

    private void sendSmsNotification(NotificationEvent event) {
        // In a real application, integrate with SMS provider like Twilio
        System.out.println("SMS notification simulated for: " + event.getRecipientPhone() + " - Message: " + event.getMessage());

        // Simulate SMS sending delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendPushNotification(NotificationEvent event) {
        // In a real application, integrate with push notification service
        System.out.println("Push notification simulated for user: " + event.getUserId() + " - Message: " + event.getMessage());
    }

    private void sendInAppNotification(NotificationEvent event) {
        // In a real application, store in database for in-app display
        System.out.println("In-app notification created for user: " + event.getUserId() + " - Message: " + event.getMessage());
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
                System.err.println("Retry failed for notification: " + event.getEventId() + " - Error: " + e.getMessage());
            }
        }
    }
}