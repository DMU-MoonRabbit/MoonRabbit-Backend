package com.bigpicture.moonrabbit.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePasswordRequestDTO {
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    @Schema(description = "현재 비밀번호", example = "asdf1234!")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 적어도 하나의 영문자와 하나의 특수 문자를 포함해야 합니다.")
    @Schema(description = "새 비밀번호", example = "newPassword123!")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인을 입력해주세요.")
    @Schema(description = "새 비밀번호 확인", example = "newPassword123!")
    private String newPasswordConfirm;

}
