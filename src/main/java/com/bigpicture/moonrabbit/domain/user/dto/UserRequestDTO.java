package com.bigpicture.moonrabbit.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRequestDTO {
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 적어도 하나의 영문자와 하나의 특수 문자를 포함해야 합니다.")
    private String password;

    private String passwordConfirm;

    @NotEmpty(message = "휴대폰 번호를 입력해주세요")
    private String phoneNum;

    private String verification;

}
