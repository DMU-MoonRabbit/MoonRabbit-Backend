package com.bigpicture.moonrabbit.domain.user.dto;

import com.bigpicture.moonrabbit.domain.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDTO {
    private String email;
    private String nickname;
    private String password;

    public UserResponseDTO(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
    }
}
