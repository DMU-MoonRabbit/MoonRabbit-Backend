package com.bigpicture.moonrabbit.domain.example.prompt;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface EntireAssistant {
    @SystemMessage("""
        당신은 전체적인 문제해결 및 고민상담 전문가입니다.
        질문에 대해 신뢰적이고 조언이 될 만한 답변을 해주세요.
        """)
    String chat(String userMessage);
}


