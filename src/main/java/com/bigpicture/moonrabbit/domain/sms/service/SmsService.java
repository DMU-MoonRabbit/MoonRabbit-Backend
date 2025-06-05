package com.bigpicture.moonrabbit.domain.sms.service;

import com.bigpicture.moonrabbit.domain.sms.dto.SmsCheckDTO;
import com.bigpicture.moonrabbit.domain.sms.dto.SmsRequestDTO;
import com.bigpicture.moonrabbit.domain.sms.dto.SmsResponseDTO;
import com.bigpicture.moonrabbit.domain.sms.entity.Sms;
import com.bigpicture.moonrabbit.domain.sms.repository.SmsRepository;
import com.bigpicture.moonrabbit.domain.sms.util.SmsCertificationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final SmsCertificationUtil smsCertificationUtil;
    private final SmsRepository smsRepository;


    @Transactional
    public SmsResponseDTO sendSms(SmsRequestDTO smsRequestDTO) {
        Sms sms = new Sms();
        SmsResponseDTO smsResponseDTO = new SmsResponseDTO();
        String phoneNum = smsRequestDTO.getPhoneNum();
        sms.setPhone(phoneNum);
        smsRepository.deleteByPhone(smsRequestDTO.getPhoneNum());
        //6자리 랜덤 번호
        String certification = Integer.toString((int)(Math.random()* (999999 - 100000 + 1)) + 100000);
        sms.setCertification(certification);
        smsRepository.save(sms);
        smsCertificationUtil.sendSms(phoneNum, certification);
        smsResponseDTO.setMessage("인증번호가 발송되었습니다.");
        smsResponseDTO.setSuccess(true);
        return smsResponseDTO;
    }

    public SmsResponseDTO checkNum(@Valid SmsCheckDTO smsCheckDTO) {
        Sms sms = smsRepository.findByPhone(smsCheckDTO.getPhoneNum());
        SmsResponseDTO smsResponseDTO = new SmsResponseDTO();
        if(sms == null) {
            smsResponseDTO.setSuccess(false);
            smsResponseDTO.setMessage("인증 요청 기록이 없습니다.");
        }else if (sms.getCertification().equals(smsCheckDTO.getCertificate())) {
            smsResponseDTO.setSuccess(true);
            smsResponseDTO.setMessage("인증에 성공했습니다.");
        }
        else {
            smsResponseDTO.setSuccess(false);
            smsResponseDTO.setMessage("인증번호가 일치하지 않습니다.");
        }
        return smsResponseDTO;
    }
}
