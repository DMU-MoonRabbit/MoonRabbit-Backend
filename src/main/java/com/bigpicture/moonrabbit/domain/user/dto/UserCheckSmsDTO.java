package com.bigpicture.moonrabbit.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCheckSmsDTO {
    private String phoneNum;
    private String certificate;
}
