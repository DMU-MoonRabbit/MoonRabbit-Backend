package com.bigpicture.moonrabbit.domain.playlist.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // 이 클래스는 JPA에서 데이터베이스 테이블과 연결되는 '엔티티'임을 나타냄
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Playlist {

    @Id // 이 필드는 테이블의 기본 키(PK)임을 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동으로 숫자 증가
    private Long id; // 고유 ID

    @Column(nullable = false) // 이 컬럼은 null이 될 수 없음
    private String title; // 영상 제목

    @Column(nullable = false)
    private String videoUrl; // 사용할 유툽 주소

    private String videoId; // 유튜브 영상 ID만 저장 (ex. abcd1234)

    private String thumbnailUrl; // 영상 썸넬 이미지

    private LocalDateTime createdAt; // 생성 시간? 필요할까
}
