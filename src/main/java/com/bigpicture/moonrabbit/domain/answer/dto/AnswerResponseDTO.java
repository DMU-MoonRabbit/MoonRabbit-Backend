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

    public AnswerResponseDTO(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.createdAt = answer.getCreatedAt();
        this.likeCount = answer.getLikeCount();
        this.reportCount = answer.getReportCount();

        // 대댓글용 parent ID
        this.parentId = answer.getParent() != null ? answer.getParent().getId() : null;

        // 작성자 정보
        this.userId = answer.getUser().getId();
        this.nickname = answer.getUser().getNickname();
        this.profileImg = answer.getUser().getProfileImg();
    }
}