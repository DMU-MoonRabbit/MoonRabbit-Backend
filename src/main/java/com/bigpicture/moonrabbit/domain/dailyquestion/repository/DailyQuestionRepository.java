package com.bigpicture.moonrabbit.domain.dailyquestion.repository;

import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyQuestionRepository extends JpaRepository<DailyQuestion,Long> {
    Optional<DailyQuestion> findByDate(LocalDate date);
}
