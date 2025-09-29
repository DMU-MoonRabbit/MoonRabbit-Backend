package com.bigpicture.moonrabbit.domain.report.entity;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportTargetType targetType; // BOARD, COMMENT

    private Long targetId; // 신고 대상의 PK (게시글 ID or 댓글 ID)

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter; // 신고한 유저

    // 신고 대상 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    private Board board;

    // 신고 대상 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", insertable = false, updatable = false)
    private Answer answer;

    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING; // 기본값 미처리

    private LocalDateTime createdAt = LocalDateTime.now();

    private String targetContent;

}
