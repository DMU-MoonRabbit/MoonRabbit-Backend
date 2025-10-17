package com.bigpicture.moonrabbit.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateNicknameRequestDTO {
    @NotBlank(message = "새로운 닉네임을 입력해주세요.")
    @Schema(description = "새로운 닉네임", example = "새로운달토끼")
    private String newNickname;
}
