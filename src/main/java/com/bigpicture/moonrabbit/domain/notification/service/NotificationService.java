package com.bigpicture.moonrabbit.domain.notification.service;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.notification.dto.NotificationResponseDTO;
import com.bigpicture.moonrabbit.domain.notification.entity.Notification;
import com.bigpicture.moonrabbit.domain.notification.entity.NotificationType;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {

    public void sendNotification(Long receiverId, NotificationResponseDTO dto);

    public void createCommentNotification(User sender, User receiver, Answer answer);

    public List<NotificationResponseDTO> getNotifications(User receiver);

    public void markAsRead(Long notificationId);

    public SseEmitter createEmitter(Long userId);


}
