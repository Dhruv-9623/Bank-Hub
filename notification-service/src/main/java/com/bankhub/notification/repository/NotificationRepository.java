package com.bankhub.notification.repository;

import com.bankhub.notification.entity.NotificationEvent;
import com.bankhub.notification.entity.NotificationStatus;
import com.bankhub.notification.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEvent, Long> {

    List<NotificationEvent> findByUserId(Long userId);

    List<NotificationEvent> findByStatus(NotificationStatus status);

    List<NotificationEvent> findByNotificationType(NotificationType notificationType);

    @Query("SELECT n FROM NotificationEvent n WHERE n.userId = ?1 ORDER BY n.createdAt DESC")
    List<NotificationEvent> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(n) FROM NotificationEvent n WHERE n.status = 'SENT' AND n.createdAt >= ?1")
    long countSentNotificationsSince(LocalDateTime since);

    @Query("SELECT COUNT(n) FROM NotificationEvent n WHERE n.status = 'FAILED' AND n.createdAt >= ?1")
    long countFailedNotificationsSince(LocalDateTime since);
}