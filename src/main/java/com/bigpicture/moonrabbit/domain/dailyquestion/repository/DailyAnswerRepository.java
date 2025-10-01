package com.bigpicture.moonrabbit.domain.dailyquestion.repository;

import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyAnswer;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyQuestion;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyAnswerRepository extends JpaRepository<DailyAnswer,Long> {
    // today 답변을 찾거나 (user + question)으로 확인할 때 사용
    Optional<DailyAnswer> findByDailyQuestionIdAndUserId(Long dailyQuestionId, Long userId);

    // User 객체 기준으로 페이징 조회, 생성일 내림차순
    Page<DailyAnswer> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
