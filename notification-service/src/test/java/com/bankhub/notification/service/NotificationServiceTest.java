package com.bankhub.notification.service;

import com.bankhub.notification.dto.NotificationRequestDto;
import com.bankhub.notification.entity.NotificationChannel;
import com.bankhub.notification.entity.NotificationEvent;
import com.bankhub.notification.entity.NotificationStatus;
import com.bankhub.notification.entity.NotificationType;
import com.bankhub.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private NotificationRequestDto requestDto;
    private NotificationEvent testNotification;

    @BeforeEach
    void setUp() {
        requestDto = new NotificationRequestDto();
        requestDto.setUserId(1L);
        requestDto.setRecipientEmail("test@example.com");
        requestDto.setNotificationType(NotificationType.TRANSACTION_ALERT);
        requestDto.setChannel(NotificationChannel.EMAIL);
        requestDto.setSubject("Test Subject");
        requestDto.setMessage("Test message");
        requestDto.setSourceEvent("test-event");
        requestDto.setEventData(Map.of("key", "value"));

        testNotification = NotificationEvent.builder()
                .id(1L)
                .userId(1L)
                .recipientEmail("test@example.com")
                .notificationType(NotificationType.TRANSACTION_ALERT)
                .channel(NotificationChannel.EMAIL)
                .subject("Test Subject")
                .message("Test message")
                .status(NotificationStatus.SENT)
                .sourceEvent("test-event")
                .build();
    }

    @Test
    void sendNotification_Email_Success() {
        when(notificationRepository.save(any(NotificationEvent.class))).thenReturn(testNotification);

        var result = notificationService.sendNotification(requestDto);

        assertNotNull(result);
        assertEquals(NotificationStatus.SENT, result.getStatus());
        assertEquals(NotificationChannel.EMAIL, result.getChannel());
        verify(notificationRepository).save(any(NotificationEvent.class));
    }

    @Test
    void sendNotification_SMS_Success() {
        requestDto.setChannel(NotificationChannel.SMS);
        requestDto.setRecipientPhone("+1234567890");

        when(notificationRepository.save(any(NotificationEvent.class))).thenAnswer(i -> {
            NotificationEvent event = i.getArgument(0);
            event.setId(1L);
            return event;
        });

        var result = notificationService.sendNotification(requestDto);

        assertNotNull(result);
        assertEquals(NotificationChannel.SMS, result.getChannel());
    }

    @Test
    void getNotificationsByUserId_Success() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(any()))
                .thenReturn(Arrays.asList(testNotification));

        var results = notificationService.getNotificationsByUserId(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Test Subject", results.get(0).getSubject());
    }

    @Test
    void getNotificationStatistics_Success() {
        when(notificationRepository.count()).thenReturn(50L);
        when(notificationRepository.countByStatus(any())).thenReturn(45L);
        when(notificationRepository.countByChannel(any())).thenReturn(30L);

        var result = notificationService.getNotificationStatistics();

        assertNotNull(result);
        assertEquals(50L, result.getTotalNotifications());
    }
}