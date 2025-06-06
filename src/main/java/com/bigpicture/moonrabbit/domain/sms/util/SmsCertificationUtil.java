package com.bigpicture.moonrabbit.domain.sms.util;


import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsCertificationUtil {

    @Value("${coolsms.apikey}")
    private String apikey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    DefaultMessageService messageService;

    @PostConstruct
    public void init() { // 의존성 주입이 완료된 후 초기화를 수행하는 메서드
        messageService = new DefaultMessageService(apikey, apiSecret, "https://api.coolsms.co.kr");
    }

    public void sendSms(String to, String certification) {
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(to);
        message.setText("본인확인 인증번호는 " + certification + "입니다.");

        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
