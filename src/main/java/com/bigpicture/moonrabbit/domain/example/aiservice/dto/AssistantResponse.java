package com.bigpicture.moonrabbit.domain.example.aiservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssistantResponse {

    @Schema(description = "AI답변", example = "진로에 대한 고민은 누구나 하는 거예요. 스스로에 대해 천천히 탐색해보는 시간을 가져보세요.")
    private String reply;

    @Schema(description = "사용자가 보낸 원본 메시지", example = "요즘 진로 고민이 많아요...")
    private String userMessage;

    @Schema(description = "카테고리", example = "MENTAL")
    private String category;
}