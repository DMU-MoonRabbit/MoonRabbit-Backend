package com.bigpicture.moonrabbit.global.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainConfig {

    @Bean
    public ChatModel chatModel() {
        String apiKey = System.getenv("OPENAI_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("환경 변수 OPENAI_API_KEY가 설정되어 있지 않습니다!");
        }

        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o-mini")
                .temperature(0.7)
                .build();
    }
}
