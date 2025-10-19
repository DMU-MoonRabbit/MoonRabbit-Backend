package com.bigpicture.moonrabbit.domain.fine.service;

import com.bigpicture.moonrabbit.domain.fine.entity.FineModelVersion;
import com.bigpicture.moonrabbit.domain.fine.repository.FineModelVersionRepository;
import com.bigpicture.moonrabbit.domain.ocr.service.OcrPdfProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FineOrchestrator {

    private final OcrPdfProcessor ocrPdfProcessor;
    private final FineDatasetService fineDatasetService;
    private final FineTuningService fineTuningService;
    private final FineModelVersionRepository versionRepository;

    public String processAndTrain(File pdfFile) {
        try {
            //  OCR → 텍스트 추출
            String text = ocrPdfProcessor.extractTextFromPdf(pdfFile);

            //  JSONL 파일 생성 (영구 저장)
            File jsonlFile = fineDatasetService.createTrainingJsonl(text, pdfFile.getName());

            // Fine-tuning 실행
            String modelName = fineTuningService.startFineTuning(jsonlFile.getAbsolutePath());

            // 모델 버전 기록
            FineModelVersion version = new FineModelVersion();
            version.setModelName(modelName);
            version.setSourceFile(pdfFile.getName());
            version.setCreatedAt(LocalDateTime.now());
            versionRepository.save(version);

            return modelName;
        } catch (Exception e) {
            throw new RuntimeException("파인튜닝 파이프라인 실패: " + e.getMessage(), e);
        }
    }
}
