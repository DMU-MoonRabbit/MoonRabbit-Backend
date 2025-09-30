package com.bigpicture.moonrabbit.domain.dailyquestion.service;

import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyAnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyAnswer;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyQuestion;
import com.bigpicture.moonrabbit.domain.dailyquestion.repository.DailyAnswerRepository;
import com.bigpicture.moonrabbit.domain.dailyquestion.repository.DailyQuestionRepository;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class DailyAnswerServiceImpl implements DailyAnswerService{
    private final DailyAnswerRepository dailyAnswerRepository;
    private final DailyQuestionRepository dailyQuestionRepository;
    private final UserService userService;

    @Transactional
    public DailyAnswerResponseDTO submitOrUpdateTodayAnswer(String answerText) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.getUserByEmail(email);

        LocalDate today = LocalDate.now();
        DailyQuestion question = dailyQuestionRepository.findByDate(today)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        // 이미 답변 존재하면 수정
        DailyAnswer dailyAnswer = dailyAnswerRepository.findByDailyQuestionIdAndUserId(question.getId(), currentUser.getId())
                .map(existing -> {
                    existing.updateAnswer(answerText);
                    return existing;
                })
                .orElseGet(() -> DailyAnswer.builder()
                        .user(currentUser)
                        .dailyQuestion(question)
                        .answer(answerText)
                        .createdAt(LocalDateTime.now())
                        .build());

        DailyAnswer saved = dailyAnswerRepository.save(dailyAnswer);
        return new DailyAnswerResponseDTO(saved.getId(), question.getId(), question.getContent(), saved.getAnswer(), saved.getCreatedAt());
    }

    /**
     * 오늘 자신이 쓴 답변 조회 (없으면 null 또는 예외 처리)
     */
    @Transactional(readOnly = true)
    public DailyAnswerResponseDTO getMyTodayAnswer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.getUserByEmail(email);

        LocalDate today = LocalDate.now();
        DailyQuestion question = dailyQuestionRepository.findByDate(today)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        DailyAnswer answer = dailyAnswerRepository.findByDailyQuestionIdAndUserId(question.getId(), currentUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_ANSWER_NOT_FOUND));

        return new DailyAnswerResponseDTO(answer.getId(), question.getId(), question.getContent(), answer.getAnswer(), answer.getCreatedAt());
    }

    /**
     * 본인의 히스토리(질문 내용 + 답변) 페이징 조회
     */
    @Transactional(readOnly = true)
    public Page<DailyAnswer> getMyAnswerHistory(User currentUser, Pageable pageable) {
        return dailyAnswerRepository.findByUserOrderByCreatedAtDesc(currentUser, pageable);
    }
}
