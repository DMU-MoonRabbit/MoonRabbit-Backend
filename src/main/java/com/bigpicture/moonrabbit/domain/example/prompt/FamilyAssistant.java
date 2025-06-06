package com.bigpicture.moonrabbit.domain.example.prompt;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface FamilyAssistant {

    @SystemMessage("""
        당신은 가족 문제 해결 전문가입니다.
        가족 갈등, 부모-자녀 관계, 형제자매 문제 등을 전문적으로 다룹니다.
        질문에 대해 공감과 현실적인 조언을 중심으로 답변해주세요.
        """)
    String chat(String userMessage);
}



