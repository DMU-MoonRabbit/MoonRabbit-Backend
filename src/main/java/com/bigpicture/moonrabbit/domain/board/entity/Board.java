package com.bigpicture.moonrabbit.domain.board.entity;

import com.bigpicture.moonrabbit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
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
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // 외래 키 설정
    private User user;  // User 엔티티와 직접 연관

    private String title;
    private String content;
    private String emotionTag;
    private int commentCount = 0;
    private boolean isAnonymous = false;
    private String aiStyle;
    private boolean isReported = false;
    @CreatedDate
    private LocalDateTime createdAt;


}
