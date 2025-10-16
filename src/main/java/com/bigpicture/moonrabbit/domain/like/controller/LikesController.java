package com.bigpicture.moonrabbit.domain.like.controller;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.like.service.LikeService;
import com.bigpicture.moonrabbit.domain.like.service.LikeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/likes")
@RequiredArgsConstructor
public class LikesController {
    private final LikeService likeService;

    @Operation(summary = "댓글 좋아요", description = "댓글 좋아요 누르는 기능")
    @PostMapping("{answerId}/answer-like")
    public ResponseEntity<AnswerResponseDTO> like(@PathVariable Long answerId, @RequestParam Long userId) {
        likeService.likeAnswer(answerId, userId);
        AnswerResponseDTO dto = likeService.getAnswerDto(answerId, userId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "댓글 좋아요취소", description = "댓글 좋아요 취소 기능")
    @DeleteMapping("/{answerId}/answer-like")
    public ResponseEntity<AnswerResponseDTO> unlike(@PathVariable Long answerId, @RequestParam Long userId) {
        likeService.unlikeAnswer(answerId, userId);
        AnswerResponseDTO dto = likeService.getAnswerDto(answerId, userId);
        return ResponseEntity.ok(dto);
    }
}
