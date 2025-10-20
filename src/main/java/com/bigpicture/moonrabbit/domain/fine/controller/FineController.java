package com.bigpicture.moonrabbit.domain.fine.controller;

import com.bigpicture.moonrabbit.domain.fine.service.FineOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/fine")
@RequiredArgsConstructor
public class FineController {

    private final FineOrchestrator fineOrchestrator;

    @PostMapping("/train")
    public ResponseEntity<String> trainFromPdf(@RequestParam("file") MultipartFile file) {
        try {
            File tempPdf = File.createTempFile("fine_input_", ".pdf");
            file.transferTo(tempPdf);

            String modelName = fineOrchestrator.processAndTrain(tempPdf);
            return ResponseEntity.ok("Fine-tuning 완료  새 모델: " + modelName);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("오류 발생: " + e.getMessage());
        }
    }
}
