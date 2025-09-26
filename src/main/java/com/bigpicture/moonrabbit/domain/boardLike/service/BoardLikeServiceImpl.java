package com.bigpicture.moonrabbit.domain.boardLike.service;

import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.boardLike.entity.BoardLike;
import com.bigpicture.moonrabbit.domain.boardLike.repository.BoardLikeRepository;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService{

    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public void likeBoard(Long boardId, Long userId) {
        if (boardLikeRepository.existsByBoardIdAndUserId(boardId, userId)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED_BOARD);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        BoardLike like = BoardLike.builder()
                .board(board)
                .user(user)
                .build();
        boardLikeRepository.save(like);
    }

    @Transactional
    public void unlikeBoard(Long boardId, Long userId) {
        boardLikeRepository.deleteByBoardIdAndUserId(boardId, userId);
    }

    @Override
    public Page<BoardResponseDTO> getLikedBoardsByUser(int page, int size) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long currentUserId = userService.getUserIdByEmail(email);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BoardLike> likedBoards = boardLikeRepository.findByUserId(currentUserId, pageable);

        return likedBoards.map(boardLike -> new BoardResponseDTO(boardLike.getBoard(), currentUserId));
    }
}
