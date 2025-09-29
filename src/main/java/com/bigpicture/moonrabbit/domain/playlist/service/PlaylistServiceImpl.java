package com.bigpicture.moonrabbit.domain.playlist.service;

import com.bigpicture.moonrabbit.domain.playlist.dto.PlaylistRequestDto;
import com.bigpicture.moonrabbit.domain.playlist.entity.Playlist;
import com.bigpicture.moonrabbit.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor // final 필드를 자동 생성자 주입
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;


    // 전체 플레이리스트 조회
    @Override
    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }


    // 플레이리스트 저장
    @Override
    public List<Playlist> saveAll(List<PlaylistRequestDto> dtoList) {

        return dtoList.stream()
                .map(dto -> {

                    String videoId = extractVideoId(dto.getVideoUrl());
                    

                    if (videoId == null) {
                        throw new IllegalArgumentException(" 유효하지 않은 유튜브 URL입니다: " + dto.getVideoUrl());
                    }

                    String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";

                    return Playlist.builder()
                            .title(dto.getTitle())
                            .videoUrl(dto.getVideoUrl())
                            .videoId(videoId)
                            .thumbnailUrl(thumbnailUrl)
                            .createdAt(LocalDateTime.now())
                            .build();
                })
                .map(playlistRepository::save)
                .toList();
    }


    // 플레이리스트 삭제
    @Override
    public void deleteAllByIds(List<Long> ids) {
        playlistRepository.deleteAllById(ids);
    }

    //플레이리스트 수정
    @Override
    public Playlist update(Long id, PlaylistRequestDto dto) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 플레이리스트가 없습니다: " + id));

        // 새로운 영상 ID 추출
        String videoId = extractVideoId(dto.getVideoUrl());
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";

        // 필드 수정
        playlist.setTitle(dto.getTitle());
        playlist.setVideoUrl(dto.getVideoUrl());
        playlist.setVideoId(videoId);
        playlist.setThumbnailUrl(thumbnailUrl);

        return playlistRepository.save(playlist);
    }

    // 유튜브 주소에서 videoId 추출 - 저장할때 호출
    String extractVideoId(String url) {

        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            for (String param : query.split("&")) {
                if (param.startsWith("v=")) {
                    return param.substring(2); // "v=abc123" → "abc123"
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }



}
