package com.bigpicture.moonrabbit.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRequestDTO {
    @NotBlank(message = "이메일은 필수입니다.")
    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @Schema(description = "초기 닉네임은 자동생성")
    private String nickname;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 적어도 하나의 영문자와 하나의 특수 문자를 포함해야 합니다.")
    @Schema(description = "비밀번호", example = "asdf1234!")
    private String password;

    @Schema(description = "비밀번호 확인", example = "asdf1234!")
    private String passwordConfirm;

    @NotEmpty(message = "휴대폰 번호를 입력해주세요")
    @Schema(description = "전화번호", example = "01043655949")
    private String phoneNum;

    @Schema(description = "인증번호", example = "123456")
    private String verification;
}
