package com.bigpicture.moonrabbit.domain.example.aiservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistantRequest {

    @Schema(description = "사용자가 보낸 고민 메시지", example = "요즘 진로 고민이 많아요...")
    private String message;
}