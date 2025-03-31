package com.bigpicture.moonrabbit.global.auth.kakao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserResponseDTO {
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;

    public KakaoUserResponseDTO(Long id, String email, String nickname, String profileImageUrl) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
