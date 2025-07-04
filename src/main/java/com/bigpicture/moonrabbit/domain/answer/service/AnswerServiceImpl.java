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


import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    public AnswerResponseDTO save(AnswerRequestDTO answerDTO, Long userId, Long boardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        Answer answer = new Answer();
        answer.setContent(answerDTO.getContent());
        answer.setUser(user);
        answer.setBoard(board);

        // 대댓글일 경우 parent 설정
        if (answerDTO.getParentId() != null) {
            Answer parent = answerRepository.findById(answerDTO.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
            answer.setParent(parent);
        }

        Answer savedAnswer = answerRepository.save(answer);
        return new AnswerResponseDTO(savedAnswer);
    }

    @Override
    public AnswerResponseDTO update(AnswerRequestDTO answerDTO, Long userId, Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!answer.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.USER_INCORRECT);
        }

        answer.setContent(answerDTO.getContent());
        Answer savedAnswer = answerRepository.save(answer);
        return new AnswerResponseDTO(savedAnswer);
    }

    @Override
    public AnswerResponseDTO delete(Long answerId, Long userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 댓글 작성자 본인만 삭제 가능하게 수정
        if (!answer.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.USER_INCORRECT);
        }

        answerRepository.delete(answer);
        return new AnswerResponseDTO(answer);
    }

    @Override
    public List<AnswerResponseDTO> getAnswersByBoard(Long boardId) {
        List<Answer> answers = answerRepository.findByBoardId(boardId);

        // 기본 정렬이 필요하다면 정렬 추가 (예: 최신순 or 부모-자식 순)
        return answers.stream()
                .map(AnswerResponseDTO::new)
                .collect(Collectors.toList());
    }
}