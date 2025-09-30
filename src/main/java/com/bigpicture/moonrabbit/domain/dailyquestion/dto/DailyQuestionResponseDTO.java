package com.bigpicture.moonrabbit.domain.dailyquestion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyQuestionResponseDTO {
    private Long id;
    private LocalDate date;
    private String content;
}
