package com.bigpicture.moonrabbit.domain.board.service;

import com.bigpicture.moonrabbit.domain.board.dto.BoardRequestDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {
    BoardResponseDTO createBoard(BoardRequestDTO boardDTO, Long userId);

    BoardResponseDTO updateBoard(Long boardId, BoardRequestDTO boardDTO, Long userId);

    BoardResponseDTO delete(Long boardId, Long userId);

    List<BoardResponseDTO> select();

    BoardResponseDTO selectOne(Long id);

    BoardResponseDTO toDto(Board board, Long currentUserId);

    Page<BoardResponseDTO> selectPagedByUser(Long userId, int page, int size);
    // 페이징 없는 전체 조회를 위한 메서드 추가
    List<BoardResponseDTO> selectAllByUser(Long userId);

    long getTotalBoardCount();

    Page<BoardResponseDTO> selectPaged(String category, int page, int size);
}
