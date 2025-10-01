package com.bigpicture.moonrabbit.domain.dailyquestion.dto;

import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DailyAnswerResponseDTO {
    private Long answerId;
    private Long questionId;
    private String questionContent;
    private String answerContent;
    private LocalDateTime answeredAt;
}
