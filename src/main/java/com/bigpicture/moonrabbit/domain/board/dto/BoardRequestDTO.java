package com.bigpicture.moonrabbit.domain.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDTO {
    @NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "제목", example = "title")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    @Schema(description = "내용", example = "content")
    private String content;

    @Schema(description = "분야", example = "전체, 가족, 연인 ...")
    private String category;

    @Schema(description = "익명여부", example = "false")
    private boolean isAnonymous;

}
