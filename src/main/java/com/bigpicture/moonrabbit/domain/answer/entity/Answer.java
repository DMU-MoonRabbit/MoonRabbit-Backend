package com.bigpicture.moonrabbit.domain.answer.entity;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // 외래 키 설정
    private User user;  // User 엔티티와 직접 연관

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private int likeCount = 0;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    private int reportCount = 0;







}
