package com.bigpicture.moonrabbit.domain.ocr.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class OcrPdfProcessor {

    private final Tesseract tesseract;

    public OcrPdfProcessor() {
        this.tesseract = new Tesseract();

        // Windows 환경에서 tessdata 경로 직접 지정
        this.tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata\\");

        // 한국어 + 영어 동시 인식
        this.tesseract.setLanguage("kor+eng");
    }

    public String extractTextFromPdf(File pdfFile) {
        StringBuilder result = new StringBuilder();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300);
                String pageText = tesseract.doOCR(image);
                result.append("\n--- Page ").append(page + 1).append(" ---\n").append(pageText);
            }

        } catch (Exception e) {
            throw new RuntimeException("PDF OCR 처리 실패: " + e.getMessage(), e);
        }

        return result.toString();
    }
}
