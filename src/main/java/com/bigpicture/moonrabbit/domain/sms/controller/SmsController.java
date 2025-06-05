package com.bigpicture.moonrabbit.domain.sms.controller;

import com.bigpicture.moonrabbit.domain.sms.dto.SmsCheckDTO;
import com.bigpicture.moonrabbit.domain.sms.dto.SmsRequestDTO;
import com.bigpicture.moonrabbit.domain.sms.dto.SmsResponseDTO;
import com.bigpicture.moonrabbit.domain.sms.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @Operation(summary = "SMS 발송", description = "전화번호를 입력받아 SMS 발송")
    @PostMapping("/send")
    public ResponseEntity<SmsResponseDTO> sendSms(@Valid SmsRequestDTO smsRequestDTO) {
        SmsResponseDTO smsResponseDTO = smsService.sendSms(smsRequestDTO);
        return new ResponseEntity<>(smsResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/check")
    @Operation(summary = "SMS 검증 테스트", description = "전화번호, 인증번호를 입력받아 인증번호 검사")
    public ResponseEntity<SmsResponseDTO> checkSms(@Valid SmsCheckDTO smsCheckDTO) {
        SmsResponseDTO smsResponseDTO = smsService.checkNum(smsCheckDTO);
        return new ResponseEntity<>(smsResponseDTO, HttpStatus.OK);
    }
}
