package com.bigpicture.moonrabbit.domain.dailyquestion.service;

import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyAnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyAnswer;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DailyAnswerService {

    public DailyAnswerResponseDTO submitOrUpdateTodayAnswer(String answerText);
    public DailyAnswerResponseDTO getMyTodayAnswer();
    public Page<DailyAnswer> getMyAnswerHistory(User currentUser, Pageable pageable);
}
