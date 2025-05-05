package com.bigpicture.moonrabbit.domain.board.controller;

import com.bigpicture.moonrabbit.domain.board.dto.BoardRequestDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.service.BoardService;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/save")
    public ResponseEntity<BoardResponseDTO> createBoard(@RequestBody BoardRequestDTO boardDTO, Long userId) {
        BoardResponseDTO responseDTO = boardService.createBoard(boardDTO, userId);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

}
