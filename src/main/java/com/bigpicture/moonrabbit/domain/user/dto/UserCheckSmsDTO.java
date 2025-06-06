package com.bigpicture.moonrabbit.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCheckSmsDTO {
    @Schema(description = "전화번호", example = "01012345678")
    private String phoneNum;
    @Schema(description = "인증번호", example = "123456")
    private String certificate;
}
