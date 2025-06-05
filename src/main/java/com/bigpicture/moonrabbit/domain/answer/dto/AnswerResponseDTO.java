package com.bigpicture.moonrabbit.domain.answer.dto;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AnswerResponseDTO {
    private Long userId;
    private Long boardId;
    private String content;
    private LocalDateTime createdAt;
    public AnswerResponseDTO(Answer answer) {
        this.userId = answer.getUser().getId();
        this.boardId = answer.getBoard().getId();
        this.content = answer.getContent();
        this.createdAt = answer.getCreatedAt();
    }
}
