package com.bigpicture.moonrabbit.domain.boardLike.controller;

import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.service.BoardService;
import com.bigpicture.moonrabbit.domain.boardLike.service.BoardLikeService;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/likes")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;
    private final BoardService boardService;
    private final UserService userService;

    @Operation(summary = "내가 좋아하는 게시글 목록", description = "내가 좋아요 누른 게시글 목록 조회")
    @GetMapping("/board-my")
    public ResponseEntity<Page<BoardResponseDTO>> getMyLikedBoards(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        Page<BoardResponseDTO> likedBoards = boardLikeService.getLikedBoardsByUser(page, size);
        return ResponseEntity.ok(likedBoards);
    }

    @Operation(summary = "게시글 좋아요", description = "게시글 좋아요 누르는 기능")
    @PostMapping("/{boardId}/board-like")
    public ResponseEntity<BoardResponseDTO> like(@PathVariable Long boardId, @RequestParam Long userId) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long currentUserId = userService.getUserIdByEmail(email);

        boardLikeService.likeBoard(boardId, currentUserId);       // 좋아요 등록
        BoardResponseDTO boardResponseDTO = boardService.selectOne(boardId); // 최신 상태 조회
        return ResponseEntity.ok(boardResponseDTO);
    }

    @Operation(summary = "게시글 좋아요 취소", description = "내가 누른 게시글 좋아요 취소하는 기능")
    @DeleteMapping("/{boardId}/board-like")
    public ResponseEntity<BoardResponseDTO> unlike(@PathVariable Long boardId, @RequestParam Long userId) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long currentUserId = userService.getUserIdByEmail(email);

        boardLikeService.unlikeBoard(boardId, userId);
        BoardResponseDTO boardResponseDTO = boardService.selectOne(boardId); // 최신 상태 조회
        return ResponseEntity.ok(boardResponseDTO);
    }
}
