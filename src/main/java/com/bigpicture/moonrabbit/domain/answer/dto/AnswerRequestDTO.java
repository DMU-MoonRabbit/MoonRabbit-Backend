package com.bigpicture.moonrabbit.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AnswerRequestDTO {
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    private Long parentId;
}
