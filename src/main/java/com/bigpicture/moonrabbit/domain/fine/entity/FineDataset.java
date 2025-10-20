package com.bigpicture.moonrabbit.domain.fine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FineDataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;      // 생성된 jsonl 파일명
    private String sourceFile;    // OCR로 추출한 원본 PDF 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_version_id")
    private FineModelVersion modelVersion;  // 어떤 모델로 학습됐는지 연결
}
