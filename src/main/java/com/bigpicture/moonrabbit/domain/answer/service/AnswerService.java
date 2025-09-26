package com.bigpicture.moonrabbit.domain.answer.service;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerRequestDTO;
import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;

import java.util.List;

public interface AnswerService {
    AnswerResponseDTO save(AnswerRequestDTO answerDTO, Long userId, Long boardId);

    AnswerResponseDTO update(AnswerRequestDTO answerDTO, Long userId, Long answerId);

    AnswerResponseDTO delete(Long answerId, Long userId);

    List<AnswerResponseDTO> getAnswersByBoard(Long boardId, Long currentUserId);

    Board selectAnswer(Long boardId, Long answerId, Long userId);
}
