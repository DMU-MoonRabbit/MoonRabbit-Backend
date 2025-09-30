package com.bigpicture.moonrabbit.domain.dailyquestion.service;

import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyQuestionRequestDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyQuestionResponseDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyQuestion;

import java.time.LocalDate;

public interface DailyQuestionService {

    public DailyQuestion createOrReplaceForDate(DailyQuestionRequestDTO dto);
    public DailyQuestion getByDate(LocalDate date);
    public DailyQuestion getToday();
}
