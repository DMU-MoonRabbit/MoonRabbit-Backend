package com.bigpicture.moonrabbit.domain.ocr.controller;

import com.bigpicture.moonrabbit.domain.ocr.service.OcrPdfProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/test/ocr")
@RequiredArgsConstructor
public class OcrTestController {

    private final OcrPdfProcessor ocrPdfProcessor;

    /**
     * PDF 파일 업로드 → OCR → AI 응답 테스트용 엔드포인트
     */
    @PostMapping
    public ResponseEntity<String> testOcrAndAi(@RequestParam("file") MultipartFile file) {
        try {
            // 1. 임시 파일 생성 및 업로드
            File tempFile = File.createTempFile("ocr-test-", ".pdf");
            file.transferTo(tempFile);

            // 2. OCR 처리
            String extractedText = ocrPdfProcessor.extractTextFromPdf(tempFile);
            System.out.println("[DEBUG] OCR Extracted Text:");
            System.out.println(extractedText);

            // 3. LangChain4j AI 응답 생성
            String aiResponse = ocrPdfProcessor.generateAiResponse(extractedText);
            System.out.println("[DEBUG] AI Response:");
            System.out.println(aiResponse);

            // 4. 결과 반환
            return ResponseEntity.ok(aiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("OCR 또는 AI 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
