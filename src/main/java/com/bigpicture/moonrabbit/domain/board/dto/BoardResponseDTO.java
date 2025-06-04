package com.bigpicture.moonrabbit.domain.board.dto;

import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDTO {
    private Long userId;
    private String title;
    private String comment;
    private String category;

    public BoardResponseDTO(Board board) {
        this.title = board.getTitle();
        this.userId = board.getUser().getId();
        this.comment = board.getContent();
        this.category = board.getCategory();
    }
}
