package com.bigpicture.moonrabbit.domain.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsCheckDTO {

    private String phoneNum;
    private String certificate;
}
