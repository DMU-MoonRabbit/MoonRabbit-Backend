package com.bigpicture.moonrabbit.domain.ocr.service;

import dev.langchain4j.model.chat.ChatModel;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class OcrPdfProcessor {

    private final ChatModel chatModel;
    private final BlockingQueue<Tesseract> tesseractPool = new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors());

    private final String tessPath;

    // 생성자에서 경로를 @Value로 주입받도록 변경
    public OcrPdfProcessor(ChatModel chatModel, @Value("${tesseract.datapath}") String tessPath) {
        this.chatModel = chatModel;
        this.tessPath = tessPath;
    }

    @PostConstruct
    public void initPool() {
        int poolSize = Runtime.getRuntime().availableProcessors();
//        String tessPath = "C:\\Program Files\\Tesseract-OCR\\tessdata\\";

        for (int i = 0; i < poolSize; i++) {
            Tesseract t = new Tesseract();
            t.setDatapath(this.tessPath);
            t.setLanguage("kor+eng");
            tesseractPool.add(t);
        }

        System.out.println("[INFO] Tesseract OCR pool initialized with " + poolSize + " instances");
    }

    public String extractTextFromPdf(File pdfFile) {
        long startTime = System.currentTimeMillis();
        System.out.println("[INFO] OCR started: " + pdfFile.getName());

        StringBuilder result = new StringBuilder();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            int totalPages = document.getNumberOfPages();
            System.out.println("[INFO] Total pages: " + totalPages);

            for (int page = 0; page < totalPages; page++) {
                long pageStart = System.currentTimeMillis();

                BufferedImage image = renderer.renderImageWithDPI(page, 300);
                Tesseract t = borrowTesseract();
                try {
                    String text = t.doOCR(image);
                    result.append("\n--- Page ").append(page + 1).append(" ---\n").append(text);
                } finally {
                    returnTesseract(t);
                }

                long pageEnd = System.currentTimeMillis();
                System.out.println("[DEBUG] OCR page " + (page + 1) + "/" + totalPages + " done (" +
                        (pageEnd - pageStart) + "ms, about " + ((pageEnd - pageStart) / 1000.0) + "s)");
            }

        } catch (Exception e) {
            throw new RuntimeException("PDF OCR failed: " + e.getMessage(), e);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("[INFO] OCR finished: " + pdfFile.getName() +
                " (total time: " + duration + "ms, about " + (duration / 1000.0) + "s)");

        return result.toString();
    }

    public String extractPageText(File pdfFile, int pageIndex) throws Exception {
        long start = System.currentTimeMillis();
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(pageIndex, 300);

            Tesseract t = borrowTesseract();
            try {
                String text = t.doOCR(image);
                long end = System.currentTimeMillis();
                System.out.println("[DEBUG] Single page OCR completed (" + (end - start) + "ms)");
                return text;
            } finally {
                returnTesseract(t);
            }
        }
    }

    public int countPages(File pdfFile) throws Exception {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            return document.getNumberOfPages();
        }
    }

    public String generateAiResponse(String extractedText) {
        if (chatModel == null) {
            throw new IllegalStateException("ChatModel is not initialized. Check LangChain configuration.");
        }
        return chatModel.chat(extractedText);
    }

    private Tesseract borrowTesseract() throws InterruptedException {
        return tesseractPool.take();
    }

    private void returnTesseract(Tesseract t) {
        tesseractPool.offer(t);
    }
}
