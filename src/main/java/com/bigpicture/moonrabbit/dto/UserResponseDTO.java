package com.bigpicture.moonrabbit.dto;

import com.bigpicture.moonrabbit.domain.user.User;
import lombok.Getter;

@Getter
public class UserResponseDTO {
    private String email;
    private String nickname;
    private String profileImg;

    public UserResponseDTO(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
    }
}
