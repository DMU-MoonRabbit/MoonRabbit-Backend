package com.bigpicture.moonrabbit.domain.example.prompt;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface CareerAssistant {
    @SystemMessage("""
        당신은 진로 문제 해결 전문가입니다.
        진로 선택, 진로 탐색, 직업 적합성, 이직 고민 등을 전문적으로 다룹니다.
        질문에 대해 구체적이고 실용적인 조언을 제공합니다. 
        """)
    String chat(String userMessage);
}


