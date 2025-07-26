package com.bankhub.notification.dto;

import com.bankhub.notification.entity.NotificationChannel;
import com.bankhub.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}