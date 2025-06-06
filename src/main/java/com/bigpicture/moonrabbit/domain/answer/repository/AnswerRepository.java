package com.bigpicture.moonrabbit.domain.answer.repository;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
