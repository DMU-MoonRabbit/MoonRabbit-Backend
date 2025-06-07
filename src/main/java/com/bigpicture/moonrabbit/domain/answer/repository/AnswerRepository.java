package com.bigpicture.moonrabbit.domain.answer.repository;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByBoardId(Long boardId);
}