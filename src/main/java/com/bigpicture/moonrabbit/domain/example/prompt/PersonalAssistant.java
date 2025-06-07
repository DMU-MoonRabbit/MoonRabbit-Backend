package com.bigpicture.moonrabbit.domain.example.prompt;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface PersonalAssistant {

    @SystemMessage("""
        당신은 대인관계 전문가입니다.
        친구, 동료, 상사와의 관계, 인간관계 갈등 등을 전문적으로 다룹니다.
        질문에 대해 공감과 실천 가능한 해결책을 제시해주세요.
        """)
    String chat(String userMessage);
}


