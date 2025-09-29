package com.bigpicture.moonrabbit.domain.admin.dto;

import lombok.Getter;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import lombok.Setter;

@Setter
@Getter
public class UserAdminResponseDTO {
    private Long id;
    private String email;
    private String nickname;
    private int point;
    private int trustPoint;
    private int totalPoint;
    private int level;
    private String createdAt;
    private String content = "";

    public UserAdminResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.point = user.getPoint();
        this.trustPoint = user.getTrustPoint();
        this.totalPoint = user.getTotalPoint();
        this.level = user.getLevel(); // 레벨 계산 메서드가 있다면 호출
        this.createdAt = user.getCreatedAt().toString();
    }
}
