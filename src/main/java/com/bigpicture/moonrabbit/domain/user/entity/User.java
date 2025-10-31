package com.bigpicture.moonrabbit.domain.user.entity;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String provider = "common";
    private String providerId;
    private String nickname;

    @Column(length = 500)
    private String profileImg;

    @Column(length = 500)
    private String refreshToken;

    private String role = ROLE_USER;
    private int level = 1;
    private int trustPoint = 0;
    private int point = 0;
    private int totalPoint = 0;
    private boolean mentor = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Answer> answers;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public void changePoint(int delta) {
        // 현재 포인트 + delta 계산 후 0 미만이면 0으로 설정
        this.point = Math.max(this.point + delta, 0);

        // delta가 양수든 음수든 누적 totalPoint는 그대로 증가 (삭제 시 음수여도 증가 X)
        if (delta > 0) {
            this.totalPoint += delta;
        }

        // 레벨 업데이트 (예: 1000점마다 레벨 1 증가)
        this.level = calculateLevel(this.totalPoint);
    }

    // 누적 포인트 30점마다 1레벨
    public int calculateLevel(int totalPoint) {
        return totalPoint / 30 + 1;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}