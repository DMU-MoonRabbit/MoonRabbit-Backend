package com.bigpicture.moonrabbit.domain.answer.dto;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.item.dto.EquippedItemDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<EquippedItemDTO> equippedItems;
    private boolean isSelected; // 선택 댓글 표시

    private boolean likedByMe;

    public AnswerResponseDTO(Answer answer, Long currentUserId, List<EquippedItemDTO> equippedItems) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.createdAt = answer.getCreatedAt();
        this.likeCount = answer.getLikeCount();
        this.reportCount = answer.getReportCount();

        // 대댓글용 parent ID
        this.parentId = answer.getParent() != null ? answer.getParent().getId() : null;

        // 작성자 정보
        this.userId = answer.getUser().getId();
        this.nickname = (answer.getBoard().isAnonymous() && answer.getUser().getId().equals(answer.getBoard().getUser().getId()))
                ? answer.getBoard().getAnonymousNickname() // (Case 1) 게시글 작성자의 댓글: 게시글의 익명 닉네임 사용
                : answer.getUser().getNickname();          // (Case 2) 일반 댓글: 댓글 작성자의 본인 닉네임 사용
        this.profileImg = answer.getUser().getProfileImg();
        this.equippedItems = equippedItems;
        this.isSelected = answer.equals(answer.getBoard().getSelectedAnswer());
        this.likedByMe = answer.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(currentUserId));
    }
}