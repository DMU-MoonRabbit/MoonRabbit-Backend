package com.bigpicture.moonrabbit.domain.board.controller;

import com.bigpicture.moonrabbit.domain.board.dto.BoardRequestDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.service.BoardService;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/boards")
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    
    @Operation(summary = "게시글 생성", description = "제목, 내용, 카테고리를 입력받아 게시글 생성")
    @PostMapping("/save")
    public ResponseEntity<BoardResponseDTO> createBoard(@Valid @RequestBody BoardRequestDTO boardDTO) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // ← subject(email)가 들어 있음

        // 이메일로 userId 조회
        Long userId = userService.getUserIdByEmail(email);
        BoardResponseDTO responseDTO = boardService.createBoard(boardDTO, userId);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "게시글 수정", description = "사용자 ID와 게시글 ID를 받아 제목, 내용, 카테고리 수정")
    @PatchMapping("/edit/{id}")
    public ResponseEntity<BoardResponseDTO> editBoard(@RequestBody BoardRequestDTO boardDTO,@PathVariable Long id) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // ← subject(email)가 들어 있음

        // 이메일로 userId 조회
        Long userId = userService.getUserIdByEmail(email);

        BoardResponseDTO responseDTO = boardService.updateBoard(id, boardDTO, userId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "게시글 삭제", description = "게시글의 ID와 사용자 ID를 받아 게시글을 삭제")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BoardResponseDTO> deleteBoard(@PathVariable Long id) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // ← subject(email)가 들어 있음

        // 이메일로 userId 조회
        Long userId = userService.getUserIdByEmail(email);
        BoardResponseDTO responseDTO = boardService.delete(id, userId);
        return new ResponseEntity<>(responseDTO ,HttpStatus.OK);
    }

    @Operation(summary = "게시글 조회", description = "모든 게시글 조회")
    @GetMapping("/list")
    public ResponseEntity<List<BoardResponseDTO>> listBoards() {
        List<BoardResponseDTO> responseDTO = boardService.select();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "특정 게시글 조회", description = "게시글의 ID를 받아 특정 게시글 조회")
    @GetMapping("/list/{id}")
    public ResponseEntity<BoardResponseDTO> listBoards(@PathVariable Long id) {
        BoardResponseDTO responseDTO = boardService.selectOne(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
