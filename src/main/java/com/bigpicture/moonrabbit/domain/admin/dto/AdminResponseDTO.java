package com.bigpicture.moonrabbit.domain.admin.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponseDTO {
    private String content;

    public AdminResponseDTO(String content) {
        this.content = content;
    }

}
