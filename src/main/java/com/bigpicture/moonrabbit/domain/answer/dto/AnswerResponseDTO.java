package com.bigpicture.moonrabbit.domain.answer.dto;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;
    private int reportCount;
    private Long parentId;
    private Long userId;
    private String nickname;
    private String profileImg;
    private boolean isSelected; // 선택 댓글 표시

    private boolean likedByMe;

    public AnswerResponseDTO(Answer answer, Long currentUserId) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.createdAt = answer.getCreatedAt();
        this.likeCount = answer.getLikeCount();
        this.reportCount = answer.getReportCount();

        // 대댓글용 parent ID
        this.parentId = answer.getParent() != null ? answer.getParent().getId() : null;

        // 작성자 정보
        this.userId = answer.getUser().getId();
        this.nickname = answer.getBoard().isAnonymous()
                ? answer.getBoard().getAnonymousNickname()
                : answer.getUser().getNickname();
        this.profileImg = answer.getUser().getProfileImg();
        this.isSelected = answer.equals(answer.getBoard().getSelectedAnswer());
        this.likedByMe = answer.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(currentUserId));
    }
}