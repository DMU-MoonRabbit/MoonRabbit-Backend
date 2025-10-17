package com.bigpicture.moonrabbit.domain.user.dto;

import com.bigpicture.moonrabbit.domain.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private int level;
    private int point;
    private int trustPoint;
    private String profileImg;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.level = user.getLevel();
        this.point = user.getPoint();
        this.trustPoint = user.getTrustPoint();
        this.profileImg = user.getProfileImg();
    }
}