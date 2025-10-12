package com.bigpicture.moonrabbit.domain.image.controller;

import com.bigpicture.moonrabbit.domain.image.entity.FileType;
import com.bigpicture.moonrabbit.domain.image.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "Image API", description = "S3 이미지 업로드, 삭제, 수정, 조회 관련 API")
public class ImageController {

    private final S3Service s3Service;

    public record UploadResponseDto(String imageUrl) {}

    // 업로드
    @Operation(summary = "이미지 업로드", description = "MultipartFile을 받아 지정된 파일 타입 폴더에 S3 업로드 후 URL 반환")
    @PostMapping("/upload/{type}")
    public UploadResponseDto upload(
            @Parameter(description = "파일 타입", example = "PROFILE, BANNER, BORDER") @PathVariable String type,
            @Parameter(description = "업로드할 파일") @RequestParam("file") MultipartFile file) {
        FileType fileType = FileType.valueOf(type.toUpperCase());
        String imageUrl = s3Service.uploadFile(file, fileType);
        return new UploadResponseDto(imageUrl);
    }

    // 삭제
    @Operation(summary = "이미지 삭제", description = "S3 URL을 받아 해당 객체 삭제")
    @DeleteMapping
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 이미지 URL") @RequestParam String url) {
        s3Service.deleteFile(url);
        return ResponseEntity.ok().build();
    }

    // 수정
    @Operation(summary = "이미지 수정", description = "기존 이미지 URL을 받아 새로운 파일로 업로드 후 기존 이미지 삭제")
    @PutMapping("/{type}")
    public UploadResponseDto update(
            @Parameter(description = "파일 타입", example = "USER_PROFILE") @PathVariable String type,
            @Parameter(description = "삭제할 기존 이미지 URL") @RequestParam String oldUrl,
            @Parameter(description = "업로드할 새로운 파일") @RequestParam("file") MultipartFile file) {
        FileType fileType = FileType.valueOf(type.toUpperCase());
        String newImageUrl = s3Service.updateFile(oldUrl, file, fileType);
        return new UploadResponseDto(newImageUrl);
    }
}
