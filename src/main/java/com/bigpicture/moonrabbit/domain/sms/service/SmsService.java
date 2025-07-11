package com.bigpicture.moonrabbit.domain.sms.service;

import com.bigpicture.moonrabbit.domain.sms.dto.SmsCheckDTO;
import com.bigpicture.moonrabbit.domain.sms.dto.SmsRequestDTO;
import com.bigpicture.moonrabbit.domain.sms.dto.SmsResponseDTO;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;

public interface SmsService {
    @Transactional
    SmsResponseDTO sendSms(SmsRequestDTO smsRequestDTO);

    String generateCertificationCode();

    SmsResponseDTO checkNum(@Valid SmsCheckDTO smsCheckDTO);
}
