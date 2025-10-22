package com.bigpicture.moonrabbit.domain.board.dto;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.item.dto.EquippedItemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BoardResponseDTO {
    private Long boardId;
    private Long userId;
    private String title;
    private String content;
    private String category;
    private List<AnswerResponseDTO> answers;
    private String nickname;
    private String profileImg;
    private Long selectedAnswerId;
    private int likeCount;
    private List<EquippedItemDTO> equippedItems;
    private boolean likedByMe;
    private boolean isAnonymous;

    public BoardResponseDTO(Board board, Long currentUserId, List<EquippedItemDTO> equippedItems, List<AnswerResponseDTO> answerDTOs) {
        this.title = board.getTitle();
        this.boardId = board.getId();
        this.userId = board.getUser().getId();
        this.content = board.getContent();
        this.category = board.getCategory();
        this.likeCount = board.getLikeCount();
        this.answers = answerDTOs;
        this.isAnonymous = board.isAnonymous();

        if (board.getSelectedAnswer() != null) {
            this.selectedAnswerId = board.getSelectedAnswer().getId();
        }
        if (board.getUser() != null) {
            this.nickname = board.isAnonymous() ? board.getAnonymousNickname() : board.getUser().getNickname();
            this.profileImg = board.getUser().getProfileImg();
            this.equippedItems = equippedItems;
        }
        if (currentUserId != null) {
            this.likedByMe = board.getLikes().stream()
                    .anyMatch(like -> like.getUser().getId().equals(currentUserId));
        } else {
            this.likedByMe = false;
        }
    }
}