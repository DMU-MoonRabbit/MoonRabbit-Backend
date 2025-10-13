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

    Page<BoardResponseDTO> selectPaged(int page, int size);

    BoardResponseDTO toDto(Board board, Long currentUserId);
}
