package com.bigpicture.moonrabbit.domain.example.aiservice;

import com.bigpicture.moonrabbit.domain.example.prompt.*;
import dev.langchain4j.service.spring.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * This is an example of using an {@link AiService}, a high-level LangChain4j API.
 */

@RestController
@RequestMapping("/assistant")
@RequiredArgsConstructor
public class AssistantController {

    private final StreamingAssistant streamingAssistant;
    private final CareerAssistant careerAssistant;
    private final FamilyAssistant familyAssistant;
    private final LoveAssistant loveAssistant;
    private final MentalAssistant mentalAssistant;
    private final PersonalAssistant personalAssistant;
    private final SocietyAssistant societyAssistant;


    @PostMapping("/career")
    public String career(@RequestBody String message) {
        return careerAssistant.chat(message);
    }

    @PostMapping("/family")
    public String family(@RequestBody String message) {
        return familyAssistant.chat(message);
    }


    @PostMapping("/love")
    public String love(@RequestBody String message) {
        return loveAssistant.chat(message);
    }

    @PostMapping("/mental")
    public String mental(@RequestBody String message) {
        return mentalAssistant.chat(message);
    }
    @PostMapping("/personal")
    public String personal(@RequestBody String message) {
        return personalAssistant.chat(message);
    }

    @PostMapping("/society")
    public String society(@RequestBody String message) {
        return familyAssistant.chat(message);
    }





    @GetMapping(value = "/streamingAssistant", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamingAssistant(
            @RequestParam(value = "message", defaultValue = "What is the current time?") String message) {
        return streamingAssistant.chat(message);
    }
}
