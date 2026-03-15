package com.bankhub.notification.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_events")
public class NotificationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", unique = true, nullable = false)
    private String eventId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "recipient_email")
    private String recipientEmail;

    @Column(name = "recipient_phone")
    private String recipientPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private NotificationChannel channel;

    @Column(name = "subject")
    private String subject;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "source_event", nullable = false)
    private String sourceEvent;

    @Column(name = "event_data", columnDefinition = "TEXT")
    private String eventData;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Version
    private Long version;

    public NotificationEvent() {}

    public NotificationEvent(String eventId, Long userId, String recipientEmail, String recipientPhone, NotificationType notificationType,
                           NotificationChannel channel, String subject, String message, NotificationStatus status, String sourceEvent, String eventData) {
        this.eventId = eventId;
        this.userId = userId;
        this.recipientEmail = recipientEmail;
        this.recipientPhone = recipientPhone;
        this.notificationType = notificationType;
        this.channel = channel;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.sourceEvent = sourceEvent;
        this.eventData = eventData;
    }

    public static NotificationEventBuilder builder() {
        return new NotificationEventBuilder();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }
    public NotificationType getNotificationType() { return notificationType; }
    public void setNotificationType(NotificationType notificationType) { this.notificationType = notificationType; }
    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }
    public String getSourceEvent() { return sourceEvent; }
    public void setSourceEvent(String sourceEvent) { this.sourceEvent = sourceEvent; }
    public String getEventData() { return eventData; }
    public void setEventData(String eventData) { this.eventData = eventData; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public static class NotificationEventBuilder {
        private Long id;
        private String eventId;
        private Long userId;
        private String recipientEmail;
        private String recipientPhone;
        private NotificationType notificationType;
        private NotificationChannel channel;
        private String subject;
        private String message;
        private NotificationStatus status = NotificationStatus.PENDING;
        private String sourceEvent;
        private String eventData;
        private LocalDateTime createdAt;
        private LocalDateTime sentAt;
        private String errorMessage;

        NotificationEventBuilder() {}

        public NotificationEventBuilder id(Long id) { this.id = id; return this; }
        public NotificationEventBuilder eventId(String eventId) { this.eventId = eventId; return this; }
        public NotificationEventBuilder userId(Long userId) { this.userId = userId; return this; }
        public NotificationEventBuilder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public NotificationEventBuilder recipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; return this; }
        public NotificationEventBuilder notificationType(NotificationType notificationType) { this.notificationType = notificationType; return this; }
        public NotificationEventBuilder channel(NotificationChannel channel) { this.channel = channel; return this; }
        public NotificationEventBuilder subject(String subject) { this.subject = subject; return this; }
        public NotificationEventBuilder message(String message) { this.message = message; return this; }
        public NotificationEventBuilder status(NotificationStatus status) { this.status = status; return this; }
        public NotificationEventBuilder sourceEvent(String sourceEvent) { this.sourceEvent = sourceEvent; return this; }
        public NotificationEventBuilder eventData(String eventData) { this.eventData = eventData; return this; }
        public NotificationEventBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public NotificationEventBuilder sentAt(LocalDateTime sentAt) { this.sentAt = sentAt; return this; }
        public NotificationEventBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }

        public NotificationEvent build() {
            NotificationEvent event = new NotificationEvent();
            event.id = this.id;
            event.eventId = this.eventId;
            event.userId = this.userId;
            event.recipientEmail = this.recipientEmail;
            event.recipientPhone = this.recipientPhone;
            event.notificationType = this.notificationType;
            event.channel = this.channel;
            event.subject = this.subject;
            event.message = this.message;
            event.status = this.status;
            event.sourceEvent = this.sourceEvent;
            event.eventData = this.eventData;
            event.createdAt = this.createdAt;
            event.sentAt = this.sentAt;
            event.errorMessage = this.errorMessage;
            return event;
        }
    }
}