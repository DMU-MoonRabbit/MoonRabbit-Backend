package com.bigpicture.moonrabbit.domain.answer.dto;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerResponseDTO {
    private Long userId;
    private Long boardId;
    private String content;
    public AnswerResponseDTO(Answer answer) {
        this.userId = answer.getUser().getId();
        this.boardId = answer.getBoard().getId();
        this.content = answer.getContent();
    }
}
