package com.bigpicture.moonrabbit.domain.example.prompt;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface LoveAssistant {

    @SystemMessage("""
        당신은 연인 문제 해결 전문가입니다.
        연애, 갈등, 커뮤니케이션 개선에 대해 전문적인 답변을 제공합니다.
        질문에 대해 친절하고 공감 있게, 현실적인 해결책을 제시해주세요.
        """)
    String chat(String userMessage);
}


