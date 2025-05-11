package com.bigpicture.moonrabbit.domain.answer.controller;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerRequestDTO;
import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.answer.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/answer")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/save")
    public ResponseEntity<AnswerResponseDTO> createAnswer(@RequestBody AnswerRequestDTO answerDTO, Long userId, Long boardId) {
        AnswerResponseDTO answerResponseDTO = answerService.save(answerDTO, userId, boardId);
        return new ResponseEntity<>(answerResponseDTO, HttpStatus.CREATED);
    }


}
