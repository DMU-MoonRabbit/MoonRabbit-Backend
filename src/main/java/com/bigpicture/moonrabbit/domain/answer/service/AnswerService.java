package com.bigpicture.moonrabbit.domain.answer.service;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerRequestDTO;
import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.answer.repository.AnswerRepository;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    public AnswerResponseDTO save(AnswerRequestDTO answerDTO, Long userId, Long boardId) {
        Answer answer = new Answer();
        answer.setContent(answerDTO.getContent());
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        answer.setBoard(board);
        answer.setUser(user);

        Answer savedAnswer = answerRepository.save(answer);

        return new AnswerResponseDTO(savedAnswer);
    }

    public AnswerResponseDTO update(AnswerRequestDTO answerDTO, Long userId, Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        answer.setContent(answerDTO.getContent());
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(user.getId() != answer.getUser().getId()) {
            throw new CustomException(ErrorCode.USER_INCORRECT);
        }
        answer.setUser(user);
        Answer savedAnswer = answerRepository.save(answer);
        return new AnswerResponseDTO(savedAnswer);
    }
    public AnswerResponseDTO delete(Long answerId, Long userId) {
        Answer deletedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.getId() != deletedAnswer.getBoard().getUser().getId()) {
            throw new CustomException(ErrorCode.USER_INCORRECT);
        }
        answerRepository.delete(deletedAnswer);
        return new AnswerResponseDTO(deletedAnswer);
    }
}
