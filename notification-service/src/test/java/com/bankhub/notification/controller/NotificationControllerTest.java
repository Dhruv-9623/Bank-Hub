package com.bankhub.notification.controller;

import com.bankhub.notification.dto.NotificationRequestDto;
import com.bankhub.notification.entity.NotificationChannel;
import com.bankhub.notification.entity.NotificationEvent;
import com.bankhub.notification.entity.NotificationStatus;
import com.bankhub.notification.entity.NotificationType;
import com.bankhub.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendNotification_Success() throws Exception {
        NotificationRequestDto dto = new NotificationRequestDto();
        dto.setUserId(1L);
        dto.setRecipientEmail("test@example.com");
        dto.setNotificationType(NotificationType.TRANSACTION_ALERT);
        dto.setChannel(NotificationChannel.EMAIL);
        dto.setSubject("Test");
        dto.setMessage("Test message");

        doNothing().when(notificationService).sendNotification(any());

        mockMvc.perform(post("/api/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void getNotificationsByUserId_Success() throws Exception {
        when(notificationService.getNotificationsByUserId(any())).thenReturn(java.util.Arrays.asList());

        mockMvc.perform(get("/api/notifications/user/1"))
                .andExpect(status().isOk());
    }
}