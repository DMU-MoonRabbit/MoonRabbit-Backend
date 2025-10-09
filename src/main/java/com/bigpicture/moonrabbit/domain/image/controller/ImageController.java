package com.bigpicture.moonrabbit.domain.image.controller;

import com.bigpicture.moonrabbit.domain.image.entity.FileType;
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

    public record UploadResponseDto(String imageUrl) {}

    // 업로드
    @PostMapping("/upload/{type}")
    public UploadResponseDto upload(
            @PathVariable String type,
            @RequestParam("file") MultipartFile file) {
        FileType fileType = FileType.valueOf(type.toUpperCase());
        String imageUrl = s3Service.uploadFile(file, fileType);
        return new UploadResponseDto(imageUrl);
    }

    // 삭제
    @DeleteMapping
    public void delete(@RequestParam String url) {
        s3Service.deleteFile(url);
    }

    // 수정
    @PutMapping("/{type}")
    public UploadResponseDto update(
            @PathVariable String type,
            @RequestParam String oldUrl,
            @RequestParam("file") MultipartFile file) {
        FileType fileType = FileType.valueOf(type.toUpperCase());
        String newImageUrl = s3Service.updateFile(oldUrl, file, fileType);
        return new UploadResponseDto(newImageUrl);
    }
}
