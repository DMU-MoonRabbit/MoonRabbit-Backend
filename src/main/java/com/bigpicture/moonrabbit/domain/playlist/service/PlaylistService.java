package com.bigpicture.moonrabbit.domain.playlist.service;

import com.bigpicture.moonrabbit.domain.playlist.dto.PlaylistRequestDto;
import com.bigpicture.moonrabbit.domain.playlist.entity.Playlist;

import java.net.URI;
import java.util.List;

public interface PlaylistService {
    // 전체 플레이리스트 조회
    List<Playlist> getAll();

    // 플레이리스트 저장
    List<Playlist> saveAll(List<PlaylistRequestDto> dtoList);

    // 플레이리스트 삭제
    void deleteAllByIds(List<Long> ids);

    //플레이리스트 수정
    Playlist update(Long id, PlaylistRequestDto dto);
}
