package com.bigpicture.moonrabbit.domain.answer.controller;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerRequestDTO;
import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.answer.service.AnswerService;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final UserService userService;

    @GetMapping("/save")
    public String save() {
        return "/save";
    }

    @PostMapping("/save")
    @Operation(summary = "댓글 생성", description = "사용자의 ID, 게시글의 ID를 입력받고 댓글 내용 생성 (parentId가 있을 경우 대댓글)")
    public ResponseEntity<AnswerResponseDTO> createAnswer(@RequestBody AnswerRequestDTO answerDTO,
                                                          @RequestParam Long boardId) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // ← subject(email)가 들어 있음

        // 이메일로 userId 조회
        Long userId = userService.getUserIdByEmail(email);
        AnswerResponseDTO answerResponseDTO = answerService.save(answerDTO, userId, boardId);
        return new ResponseEntity<>(answerResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "댓글 수정", description = "댓글 ID와 사용자의 ID를 입력받아 댓글 내용 수정")
    @PatchMapping("/edit/{id}")
    public ResponseEntity<AnswerResponseDTO> updateAnswer(@PathVariable Long id,@RequestBody AnswerRequestDTO answerDTO) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // ← subject(email)가 들어 있음

        // 이메일로 userId 조회
        Long userId = userService.getUserIdByEmail(email);
        AnswerResponseDTO answerResponseDTO = answerService.update(answerDTO, userId, id);
        return new ResponseEntity<>(answerResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 ID와 사용자의 ID를 입력받아 댓글 삭제")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AnswerResponseDTO> deleteAnswer(@PathVariable Long id) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // ← subject(email)가 들어 있음

        // 이메일로 userId 조회
        Long userId = userService.getUserIdByEmail(email);
        AnswerResponseDTO answerResponseDTO = answerService.delete(id, userId);
        return new ResponseEntity<>(answerResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "게시글 댓글 조회", description = "게시글 ID로 댓글 목록 조회 (닉네임, 프로필, 부모ID 포함)")
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<AnswerResponseDTO>> getAnswersByBoard(@PathVariable Long boardId) {
        List<AnswerResponseDTO> answers = answerService.getAnswersByBoard(boardId);
        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

}