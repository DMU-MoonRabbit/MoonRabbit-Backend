package com.bigpicture.moonrabbit.domain.dailyquestion.entity;


import com.bigpicture.moonrabbit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DailyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 질문 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_question_id", nullable = false)
    private DailyQuestion dailyQuestion;

    @Column(nullable = false, length = 2000)
    private String answer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void updateAnswer(String newAnswer) {
        this.answer = newAnswer;
    } // User 엔티티는 프로젝트에 이미 있다고 가정합니다.
}
