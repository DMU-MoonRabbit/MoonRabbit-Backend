package com.bigpicture.moonrabbit.domain.dailyquestion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DailyAnswerHistoryResponseDTO {
    private Long questionId;
    private LocalDate questionDate;
    private String questionContent;
    private String answerContent;
    private LocalDateTime answeredAt;
}
