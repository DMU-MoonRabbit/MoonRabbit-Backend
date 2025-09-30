package com.bigpicture.moonrabbit.domain.dailyquestion.controller;

import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyQuestionRequestDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyQuestion;
import com.bigpicture.moonrabbit.domain.dailyquestion.service.DailyQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/daily-question")
@RequiredArgsConstructor
public class AdminDailyQuestionController {
    private final DailyQuestionService dailyQuestionService;

    @Operation(summary = "어드민 질문 생성", description = "관리자가 질문 생성")
    @PostMapping("/api/admin/daily-questions")
    public DailyQuestion createDailyQuestion(
            @RequestBody DailyQuestionRequestDTO requestDTO) {
        return dailyQuestionService.createOrReplaceForDate(requestDTO);
    }
}
