package com.bigpicture.moonrabbit.domain.sms.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequestDTO {
    @NotEmpty(message = "휴대폰 번호를 입력해주세요")
    @Schema(description = "전화번호", example = "01012345678")
    private String phoneNum;
}
