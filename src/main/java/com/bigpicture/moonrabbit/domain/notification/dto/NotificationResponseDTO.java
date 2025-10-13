package com.bigpicture.moonrabbit.domain.notification.dto;

import com.bigpicture.moonrabbit.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;               // 알림 ID
    private String senderName;     // 댓글 작성자 닉네임 (또는 이메일)
    private String commentContent; // 댓글 내용
    private Long boardId;          // 게시글 ID (알림 클릭 시 이동용)
    private boolean isRead;        // 읽음 여부
    private LocalDateTime createdAt; // 생성 시각

    // 엔티티 -> DTO 변환 메서드
    public static NotificationResponseDTO fromEntity(Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getSender().getNickname(), // User 엔티티에 nickname 있다고 가정
                notification.getAnswer().getContent(),
                notification.getAnswer().getBoard().getId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
