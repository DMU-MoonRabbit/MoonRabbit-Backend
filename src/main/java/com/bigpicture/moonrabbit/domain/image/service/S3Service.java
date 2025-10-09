package com.bigpicture.moonrabbit.domain.image.service;

import com.bigpicture.moonrabbit.domain.image.entity.FileType;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucket;

    public S3Service(S3Client s3Client, @Value("${aws.s3.bucket}") String bucket) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    // 업로드
    public String uploadFile(MultipartFile file, FileType fileType) {
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }

        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null) originalName = "file";

            String key = fileType.getPrefix() + UUID.randomUUID() + "_" +
                    URLEncoder.encode(originalName, StandardCharsets.UTF_8);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            return s3Client.utilities().getUrl(getUrlRequest).toString();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    // 삭제
    public void deleteFile(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.IMAGE_DELETE_FAILED);
        }
    }

    // 수정 (업로드 후 이전 파일 삭제)
    public String updateFile(String oldFileUrl, MultipartFile newFile, FileType fileType) {
        String newFileUrl = uploadFile(newFile, fileType);

        if (oldFileUrl != null && !oldFileUrl.isBlank()) {
            try {
                deleteFile(oldFileUrl);
            } catch (CustomException e) {
            }
        }

        return newFileUrl;
    }

    // S3 URL에서 키 추출
    private String extractKeyFromUrl(String fileUrl) {
        try {
            URI uri = new URI(fileUrl);
            String path = uri.getRawPath();
            return path.substring(1);
        } catch (URISyntaxException e) {
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

}