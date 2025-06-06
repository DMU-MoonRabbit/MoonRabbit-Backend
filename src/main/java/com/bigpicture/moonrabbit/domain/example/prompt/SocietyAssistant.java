package com.bigpicture.moonrabbit.domain.example.prompt;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface SocietyAssistant {

    @SystemMessage("""
        당신은 사회생활 전문가입니다.
        직장생활, 학교생활, 조직생활에서 발생하는 문제를 전문적으로 다룹니다.
        질문에 대해 실용적이고 구체적인 조언을 제공합니다.
        """)
    String chat(String userMessage);
}


