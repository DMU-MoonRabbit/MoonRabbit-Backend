package com.bigpicture.moonrabbit.domain.notification.repository;

import com.bigpicture.moonrabbit.domain.notification.entity.Notification;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverOrderByCreatedAtDesc(User receiver);
    // 생성일(createdAt) 기준으로 가장 최신 알림 1개 조회
    Notification findTopByOrderByCreatedAtDesc();
}
