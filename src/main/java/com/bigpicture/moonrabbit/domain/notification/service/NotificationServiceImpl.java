package com.bigpicture.moonrabbit.domain.notification.service;


import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.notification.dto.NotificationResponseDTO;
import com.bigpicture.moonrabbit.domain.notification.entity.Notification;
import com.bigpicture.moonrabbit.domain.notification.repository.NotificationRepository;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void createCommentNotification(User sender, User receiver, Answer answer) {
        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .answer(answer)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    public List<NotificationResponseDTO> getNotifications(User receiver) {
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(receiver)
                .stream()
                .map(NotificationResponseDTO::fromEntity)
                .toList();
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    public SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30분
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        return emitter;
    }

    public void sendNotification(Long receiverId, NotificationResponseDTO dto) {
        SseEmitter emitter = emitters.get(receiverId);
        if (emitter != null) {
            try {
                emitter.send(dto, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitters.remove(receiverId);
            }
        }
    }

}
