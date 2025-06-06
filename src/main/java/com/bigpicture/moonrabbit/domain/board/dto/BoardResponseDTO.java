package com.bigpicture.moonrabbit.domain.board.dto;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BoardResponseDTO {
    private Long userId;
    private String title;
    private String content;
    private String category;
    private List<AnswerResponseDTO> answers;


    public BoardResponseDTO(Board board) {
        this.title = board.getTitle();
        this.userId = board.getUser().getId();
        this.content = board.getContent();
        this.category = board.getCategory();
        this.answers = board.getAnswers().stream()
                .map(AnswerResponseDTO::new)
                .collect(Collectors.toList());
    }
}
