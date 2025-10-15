package com.bigpicture.moonrabbit.domain.ocr.service;

import dev.langchain4j.model.chat.ChatModel;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class OcrPdfProcessor {

    private final Tesseract tesseract;
    private final ChatModel chatModel;

    public OcrPdfProcessor(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.tesseract = new Tesseract();
        this.tesseract.setDatapath("C:\\\\Program Files\\\\Tesseract-OCR\\\\tessdata\\\\");
        this.tesseract.setLanguage("kor+eng");
    }

    // PDF에서 텍스트 추출
    public String extractTextFromPdf(File pdfFile) {
        StringBuilder result = new StringBuilder();
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300);
                String pageText = tesseract.doOCR(image);
                result.append("\\n--- Page ").append(page + 1).append(" ---\\n").append(pageText);
            }
        } catch (Exception e) {
            throw new RuntimeException("PDF OCR 처리 실패: " + e.getMessage(), e);
        }
        return result.toString();
    }

    // OCR 결과 기반 AI 응답 생성
    public String generateAiResponse(String extractedText) {
        if (chatModel == null) {
            throw new IllegalStateException("ChatModel이 초기화되지 않았습니다. LangChain 설정을 확인하세요.");
        }

        // 1.0.1 기준 LangChain4j의 공식 메서드
        return chatModel.chat(extractedText);
    }
}
