package com.bigpicture.moonrabbit.domain.example.aiservice.controller;

import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.example.aiservice.dto.AssistantRequest;
import com.bigpicture.moonrabbit.domain.example.aiservice.dto.AssistantResponse;
import com.bigpicture.moonrabbit.domain.example.aiservice.enums.AssistantCategory;
import com.bigpicture.moonrabbit.domain.example.aiservice.entity.AssistantReply;
import com.bigpicture.moonrabbit.domain.example.aiservice.service.AssistantReplyService;
import com.bigpicture.moonrabbit.domain.example.prompt.*;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board/{boardId}/assistant")
@RequiredArgsConstructor
public class AssistantController {

    private final CareerAssistant careerAssistant;
    private final FamilyAssistant familyAssistant;
    private final LoveAssistant loveAssistant;
    private final MentalAssistant mentalAssistant;
    private final PersonalAssistant personalAssistant;
    private final SocietyAssistant societyAssistant;

    private final AssistantReplyService assistantReplyService;
    private final BoardRepository boardRepository;

    @Operation(
            summary = "AI 답변 요청",
            description = "특정 게시글(boardId)에 대해 카테고리별 AI에게 본문을 보내고 답변을 생성\n" +
                    "카테고리 영문 명: CAREER, FAMILY, LOVE, MENTAL, PERSONAL, SOCIETY"
    )
    @PostMapping("/{category}")
    public AssistantResponse handleAssistantReply(
            @PathVariable Long boardId,
            @PathVariable AssistantCategory category,
            @RequestBody AssistantRequest request
    ) {
        String reply;

        switch (category) {
            case CAREER -> reply = careerAssistant.chat(request.getMessage());
            case FAMILY -> reply = familyAssistant.chat(request.getMessage());
            case LOVE -> reply = loveAssistant.chat(request.getMessage());
            case MENTAL -> reply = mentalAssistant.chat(request.getMessage());
            case PERSONAL -> reply = personalAssistant.chat(request.getMessage());
            case SOCIETY -> reply = societyAssistant.chat(request.getMessage());
            default -> throw new CustomException(ErrorCode.INVALID_ASSISTANT_CATEGORY);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        AssistantReply replyEntity = AssistantReply.builder()
                .board(board)
                .category(category)
                .userMessage(request.getMessage())
                .assistantResponse(reply)
                .build();

        assistantReplyService.saveReply(replyEntity);

        return AssistantResponse.builder()
                .reply(reply)
                .userMessage(request.getMessage())
                .category(category.name())
                .build();
    }

    @Operation(
            summary = "AI 답변 조회",
            description = "특정 게시글(boardId)에 대해 저장된 AI 답변을 조회"
    )
    @GetMapping
    public AssistantResponse getAssistantReply(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long boardId
    ) {
        AssistantReply reply = assistantReplyService.getReplyByBoardId(boardId);

        return AssistantResponse.builder()
                .reply(reply.getAssistantResponse())
                .userMessage(reply.getUserMessage())
                .category(reply.getCategory().name())
                .build();
    }
}