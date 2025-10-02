package com.bigpicture.moonrabbit.domain.answer.service;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerRequestDTO;
import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.answer.repository.AnswerRepository;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.notification.dto.NotificationResponseDTO;
import com.bigpicture.moonrabbit.domain.notification.repository.NotificationRepository;
import com.bigpicture.moonrabbit.domain.notification.service.NotificationService;
import com.bigpicture.moonrabbit.domain.point.Point;
import com.bigpicture.moonrabbit.domain.trust.Trust;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository; 
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @Override
    public AnswerResponseDTO save(AnswerRequestDTO answerDTO, Long userId, Long boardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        Answer answer = new Answer();
        answer.setContent(answerDTO.getContent());
        // 댓글 작성 시 5점 지급
        user.changePoint(Point.CREATE_ANSWER.getValue());
        answer.setUser(user);
        answer.setBoard(board);

        // 대댓글일 경우 parent 설정
        if (answerDTO.getParentId() != null) {
            Answer parent = answerRepository.findById(answerDTO.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
            answer.setParent(parent);
        }

        Answer savedAnswer = answerRepository.save(answer);

        if (!board.getUser().getId().equals(userId)) { // 자기 댓글이면 제외
            notificationService.createCommentNotification(
                    user,                 // 작성자
                    board.getUser(),       // 게시글 작성자
                    savedAnswer
            );

            NotificationResponseDTO dto = NotificationResponseDTO.fromEntity(
                    notificationRepository.findTopByOrderByCreatedAtDesc() // 마지막 저장된 알림
            );
            notificationService.sendNotification(board.getUser().getId(), dto);
        }

        return new AnswerResponseDTO(savedAnswer, user.getId());
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
        return new AnswerResponseDTO(savedAnswer, user.getId());
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

        user.changePoint(Point.DELETE_ANSWER.getValue());
        answerRepository.delete(answer);
        return new AnswerResponseDTO(answer, user.getId());
    }

    @Override
    public List<AnswerResponseDTO> getAnswersByBoard(Long boardId, Long currentUserId) {
        List<Answer> answers = answerRepository.findByBoardId(boardId);

        // 기본 정렬이 필요하다면 정렬 추가 (예: 최신순 or 부모-자식 순)
        return answers.stream()
                .map(answer -> new AnswerResponseDTO(answer, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Board selectAnswer(Long boardId, Long answerId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 게시글 작성자 확인
        if (!board.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        // 이미 선택된 댓글이 있으면 중복 채택 방지
        if (board.getSelectedAnswer() != null) {
            throw new CustomException(ErrorCode.ALREADY_SELECTED_ANSWER);
        }

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        // 게시글 작성자가 본인의 댓글을 선택하려고 하면 예외 처리
        if (answer.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.CANNOT_SELECT_OWN_COMMENT);
        }


        // 선택 댓글 설정
        board.setSelectedAnswer(answer);

        // 댓글 작성자에게 포인트 지급 (신뢰도, 포인트)
        User answerUser = answer.getUser();
        answerUser.setTrustPoint(answerUser.getTrustPoint() + Trust.ANSWER_ACCEPTED.getValue());
        answerUser.changePoint(Point.ANSWER_ACCEPTED.getValue());
        userRepository.save(answerUser); // 변경 사항 저장

        return boardRepository.save(board);  // 선택 댓글 반영
    }
}