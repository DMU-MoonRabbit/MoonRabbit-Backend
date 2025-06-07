package com.bigpicture.moonrabbit.domain.example.prompt;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface MentalAssistant {

    @SystemMessage("""
        당신은 정신건강 전문가입니다.
        스트레스, 우울증, 불안, 불면증, 자기관리 등의 문제를 전문적으로 다룹니다.
        질문에 대해 따뜻하고 안전한 분위기로 조언해주세요.
        """)
    String chat(String userMessage);
}



