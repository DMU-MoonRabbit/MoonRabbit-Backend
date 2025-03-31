package com.bigpicture.moonrabbit.domain.playlist.controller;

import com.bigpicture.moonrabbit.domain.playlist.dto.PlaylistRequestDto;
import com.bigpicture.moonrabbit.domain.playlist.entity.Playlist;
import com.bigpicture.moonrabbit.domain.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스는 REST API 처리용 컨트롤러임을 나타냄
@RequestMapping("/api/playlists") // 모든 요청은 /api/playlists로 시작됨
@RequiredArgsConstructor // 생성자 주입을 자동으로 만들어줌
public class PlaylistController {

    private final PlaylistService playlistService;

    // 플레이리스트 등록
    @PostMapping("/bulk")
    public List<Playlist> createPlaylists(@RequestBody List<PlaylistRequestDto> dtoList) {
        return playlistService.saveAll(dtoList);
    }


    // 플레이리스트 전체 조회
    @GetMapping
    public List<Playlist> getAllPlaylists() {
        return playlistService.getAll();
    }

    // 플레이리스트 삭제 (인덱스 넣는 형식으로)
    @DeleteMapping
    public void deletePlaylists(@RequestBody List<Long> ids) {
         playlistService.deleteAllByIds(ids);
    }
}
