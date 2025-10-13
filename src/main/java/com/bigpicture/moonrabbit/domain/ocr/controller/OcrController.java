package com.bigpicture.moonrabbit.domain.ocr.controller;

import com.bigpicture.moonrabbit.domain.ocr.dto.OcrResponseDTO;
import com.bigpicture.moonrabbit.domain.ocr.service.OcrPdfProcessor;
import com.bigpicture.moonrabbit.domain.example.aiservice.StreamingAssistant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class OcrController {

    private final OcrPdfProcessor ocrPdfProcessor;
    private final StreamingAssistant streamingAssistant;

    @PostMapping("/analyze")
    public ResponseEntity<OcrResponseDTO> analyzeCounselingPdf(@RequestParam("file") MultipartFile file) throws IOException {
        // 1. 임시 파일로 저장
        File tempFile = File.createTempFile("ocr-", ".pdf");
        file.transferTo(tempFile);

        // 2. OCR 텍스트 추출
        String extractedText = ocrPdfProcessor.extractTextFromPdf(tempFile);

        // 3. AI 프롬프트 구성
        String prompt = """
        This document is a scanned academic paper about psychological counseling.

        Analyze the document and provide:
        1. A summary in 5 bullet points
        2. The main counseling techniques mentioned
        3. Potential real-life applications for teenagers

        --- DOCUMENT CONTENT ---
        %s
        """.formatted(extractedText);

        // 4. Flux<String> → String 변환 (LangChain4j 기반)
        String aiResponse = streamingAssistant
                .chat(prompt)
                .collectList()
                .map(strings -> String.join("", strings))
                .block();

        // 5. 응답 반환
        return ResponseEntity.ok(new OcrResponseDTO(aiResponse));
    }
}
