package com.bigpicture.moonrabbit.domain.board.entity;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.boardLike.entity.BoardLike;
import com.bigpicture.moonrabbit.domain.example.aiservice.entity.AssistantReply;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // 외래 키 설정
    private User user;  // User 엔티티와 직접 연관

    private String title;
    private String content;
    private String category;
    private int commentCount = 0;
    private boolean isAnonymous = false;
    private String anonymousNickname;
    private String aiStyle; 
    private int reportCount = 0;
    private boolean isReported = false;
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Answer> answers = new ArrayList<>();

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private AssistantReply assistantReply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_answer_id")
    private Answer selectedAnswer; // 글쓴이가 선택한 댓글

    @CreatedDate
    private LocalDateTime createdAt;
    // 총 좋아요 개수 getter
    public int getLikeCount() {
        return likes.size();
    }
}
