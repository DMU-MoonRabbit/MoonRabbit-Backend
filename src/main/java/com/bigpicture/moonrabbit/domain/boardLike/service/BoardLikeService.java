package com.bigpicture.moonrabbit.domain.boardLike.service;

import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import org.springframework.data.domain.Page;

public interface BoardLikeService {

    void likeBoard(Long boardId, Long userId);

    void unlikeBoard(Long boardId, Long userId);

    Page<BoardResponseDTO> getLikedBoardsByUser(int page, int size);
}
