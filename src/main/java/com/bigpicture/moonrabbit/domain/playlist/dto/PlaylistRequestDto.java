package com.bigpicture.moonrabbit.domain.playlist.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaylistRequestDto {
    private String title;
    private String videoUrl;
}
