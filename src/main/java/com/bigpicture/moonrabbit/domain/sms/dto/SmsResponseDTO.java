package com.bigpicture.moonrabbit.domain.sms.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsResponseDTO {
    private boolean success;
    private String message;
}
