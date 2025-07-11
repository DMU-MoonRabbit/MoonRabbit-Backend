package com.bigpicture.moonrabbit.domain.example.aiservice;

import dev.langchain4j.agent.tool.Tool;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class AssistantTools {

    @Tool
    @Observed
    public String currentTime() {
        return LocalTime.now().toString();
    }
}
