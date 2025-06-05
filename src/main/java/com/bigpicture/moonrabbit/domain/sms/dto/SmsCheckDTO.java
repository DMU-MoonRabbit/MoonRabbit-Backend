package com.bigpicture.moonrabbit.domain.sms.dto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsCheckDTO {

    @Schema(description = "전화번호", example = "01012345678")
    private String phoneNum;
    private String certification;
}
