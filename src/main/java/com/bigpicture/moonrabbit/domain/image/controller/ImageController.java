package com.bigpicture.moonrabbit.domain.image.controller;

import com.bigpicture.moonrabbit.domain.image.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(s3Service.uploadFile(file));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String url) {
        s3Service.deleteFile(url);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestParam String oldUrl, @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(s3Service.updateFile(oldUrl, file));
    }
}