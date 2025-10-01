package com.bigpicture.moonrabbit.domain.dailyquestion.service;


import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyQuestionRequestDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyQuestionResponseDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyAnswer;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyQuestion;
import com.bigpicture.moonrabbit.domain.dailyquestion.repository.DailyAnswerRepository;
import com.bigpicture.moonrabbit.domain.dailyquestion.repository.DailyQuestionRepository;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
public class DailyQuestionServiceImpl implements DailyQuestionService{
    private final DailyQuestionRepository dailyQuestionRepository;

    @Transactional
    public DailyQuestion createOrReplaceForDate(DailyQuestionRequestDTO dto) {
        LocalDate date = dto.getDate();
        String content = dto.getContent();

        DailyQuestion q = dailyQuestionRepository.findByDate(date)
                .map(existing -> {
                    existing.updateContent(content);
                    return existing;
                })
                .orElseGet(() -> DailyQuestion.builder().date(date).content(content).build());

        return dailyQuestionRepository.save(q);
    }

    @Transactional(readOnly = true)
    public DailyQuestion getByDate(LocalDate date) {
        return dailyQuestionRepository.findByDate(date)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public DailyQuestion getToday() {
        return getByDate(LocalDate.now());
    }

}
