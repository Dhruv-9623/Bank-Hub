package com.bankhub.notification.controller;

import com.bankhub.notification.dto.NotificationRequestDto;
import com.bankhub.notification.entity.NotificationEvent;
import com.bankhub.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestBody NotificationRequestDto request) {

        System.out.println("Manual notification request: " + request.getNotificationType() + " for user: " + request.getUserId());

        try {
            notificationService.sendNotification(request);
            return ResponseEntity.ok("Notification sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send notification: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationEvent>> getUserNotifications(
            @PathVariable Long userId) {

        System.out.println("Fetching notifications for user: " + userId);

        List<NotificationEvent> notifications = notificationService.getUserNotifications(userId);

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/failed")
    public ResponseEntity<List<NotificationEvent>> getFailedNotifications() {

        System.out.println("Fetching failed notifications");

        List<NotificationEvent> failedNotifications = notificationService.getFailedNotifications();

        return ResponseEntity.ok(failedNotifications);
    }

    @PostMapping("/retry-failed")
    public ResponseEntity<String> retryFailedNotifications() {

        System.out.println("Retrying failed notifications");

        try {
            notificationService.retryFailedNotifications();
            return ResponseEntity.ok("Failed notifications retry initiated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to retry notifications: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getNotificationStats() {

        System.out.println("Fetching notification statistics");

        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        long sentCount = notificationService.getSentNotificationsCount(oneDayAgo);
        long failedCount = notificationService.getFailedNotificationsCount(oneDayAgo);

        Map<String, Object> stats = Map.of(
                "sentNotifications24h", sentCount,
                "failedNotifications24h", failedCount,
                "successRate", sentCount + failedCount > 0 ? (double) sentCount / (sentCount + failedCount) * 100 : 100.0,
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Notification Service is working! 📧🔔");
    }
}