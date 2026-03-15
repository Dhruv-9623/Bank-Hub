package com.bankhub.notification.dto;

import com.bankhub.notification.entity.NotificationChannel;
import com.bankhub.notification.entity.NotificationType;

public class NotificationRequestDto {
    private Long userId;
    private String recipientEmail;
    private String recipientPhone;
    private NotificationType notificationType;
    private NotificationChannel channel;
    private String subject;
    private String message;
    private String sourceEvent;
    private String eventData;

    public NotificationRequestDto() {}

    public NotificationRequestDto(Long userId, String recipientEmail, String recipientPhone, NotificationType notificationType,
                                 NotificationChannel channel, String subject, String message, String sourceEvent, String eventData) {
        this.userId = userId;
        this.recipientEmail = recipientEmail;
        this.recipientPhone = recipientPhone;
        this.notificationType = notificationType;
        this.channel = channel;
        this.subject = subject;
        this.message = message;
        this.sourceEvent = sourceEvent;
        this.eventData = eventData;
    }

    public static NotificationRequestDtoBuilder builder() { return new NotificationRequestDtoBuilder(); }

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
    public String getSourceEvent() { return sourceEvent; }
    public void setSourceEvent(String sourceEvent) { this.sourceEvent = sourceEvent; }
    public String getEventData() { return eventData; }
    public void setEventData(String eventData) { this.eventData = eventData; }

    public static class NotificationRequestDtoBuilder {
        private Long userId;
        private String recipientEmail;
        private String recipientPhone;
        private NotificationType notificationType;
        private NotificationChannel channel;
        private String subject;
        private String message;
        private String sourceEvent;
        private String eventData;

        NotificationRequestDtoBuilder() {}

        public NotificationRequestDtoBuilder userId(Long userId) { this.userId = userId; return this; }
        public NotificationRequestDtoBuilder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public NotificationRequestDtoBuilder recipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; return this; }
        public NotificationRequestDtoBuilder notificationType(NotificationType notificationType) { this.notificationType = notificationType; return this; }
        public NotificationRequestDtoBuilder channel(NotificationChannel channel) { this.channel = channel; return this; }
        public NotificationRequestDtoBuilder subject(String subject) { this.subject = subject; return this; }
        public NotificationRequestDtoBuilder message(String message) { this.message = message; return this; }
        public NotificationRequestDtoBuilder sourceEvent(String sourceEvent) { this.sourceEvent = sourceEvent; return this; }
        public NotificationRequestDtoBuilder eventData(String eventData) { this.eventData = eventData; return this; }

        public NotificationRequestDto build() {
            return new NotificationRequestDto(userId, recipientEmail, recipientPhone, notificationType, channel, subject, message, sourceEvent, eventData);
        }
    }
}