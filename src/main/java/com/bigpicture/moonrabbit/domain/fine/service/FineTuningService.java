package com.bigpicture.moonrabbit.domain.fine.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.File;
import java.util.Map;

@Service
@Slf4j
public class FineTuningService {

    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String BASE_URL = "https://api.openai.com/v1";

    private final WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader("Authorization", "Bearer " + OPENAI_API_KEY)
            .build();

    /**
     * Start fine-tuning with default base model
     */
    public String startFineTuning(String jsonlPath) {
        return startFineTuningWithBaseModel(jsonlPath, "gpt-3.5-turbo");
    }

    /**
     * Upload JSONL file, create fine-tuning job, and monitor until completion
     */
    public String startFineTuningWithBaseModel(String jsonlPath, String baseModel) {
        try {
            File jsonlFile = new File(jsonlPath);

            if (!jsonlFile.exists()) {
                log.error("[FineTuning] JSONL file not found: {}", jsonlPath);
                return null;
            }

            log.info("[FineTuning] Starting upload: {}", jsonlFile.getAbsolutePath());
            log.info("[FineTuning] File size: {} bytes", jsonlFile.length());

            // Step 1: Upload JSONL file
            MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();

            // FileSystemResource를 사용하여 스트리밍 방식으로 전송 (메모리 효율성 향상)
            FileSystemResource resource = new FileSystemResource(jsonlFile);

            multipartBody.add("file", resource);
            multipartBody.add("purpose", "fine-tune");

            Map<String, Object> uploadResponse = webClient.post()
                    .uri("/files")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(multipartBody))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (uploadResponse == null || !uploadResponse.containsKey("id")) {
                log.error("[FineTuning] File upload failed: empty response from API");
                return null;
            }

            String fileId = (String) uploadResponse.get("id");
            log.info("[FineTuning] File uploaded successfully. File ID: {}", fileId);

            // Step 2: Create fine-tuning job
            Map<String, Object> jobRequest = Map.of(
                    "model", baseModel,
                    "training_file", fileId,
                    "suffix", "moonrabbit"
            );

            Map<String, Object> jobResponse = webClient.post()
                    .uri("/fine_tuning/jobs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jobRequest)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (jobResponse == null || !jobResponse.containsKey("id")) {
                log.error("[FineTuning] Job creation failed: empty response from API");
                return null;
            }

            String jobId = (String) jobResponse.get("id");
            String status = (String) jobResponse.get("status");
            Object fineTunedModel = jobResponse.get("fine_tuned_model");

            log.info("[FineTuning] Job created");
            log.info("  - Job ID: {}", jobId);
            log.info("  - Status: {}", status);
            log.info("  - Fine-tuned Model: {}", fineTunedModel);

            // Step 3: Wait for job completion and return final model name
            String completedModel = waitForFineTuningCompletion(jobId);
            if (completedModel != null) {
                log.info("[FineTuning] Fine-tuning completed successfully. New model: {}", completedModel);
            } else {
                log.warn("[FineTuning] Fine-tuning did not complete successfully or timed out.");
            }

            return completedModel;

        } catch (WebClientResponseException e) { // 👈 WebClientResponseException 추가
            log.error("[FineTuning] Error during execution: {} from {}", e.getStatusCode(), e.getRequest().getURI());
            log.error("[FineTuning] OpenAI Error Body: {}", e.getResponseBodyAsString());
            return null; // 에러 발생 시 null 반환
        } catch (Exception e) {
            log.error("[FineTuning] Error during execution: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Polls the fine-tuning job status until completion or failure
     */
    private String waitForFineTuningCompletion(String jobId) {
        try {
            log.info("[FineTuning] Monitoring fine-tuning job status: {}", jobId);
            for (int i = 0; i < 60; i++) { // up to 10 minutes total (60 * 10s)
                Map<String, Object> statusResponse = webClient.get()
                        .uri("/fine_tuning/jobs/" + jobId)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

                if (statusResponse == null) {
                    log.warn("[FineTuning] Empty status response from API");
                    continue;
                }

                String status = (String) statusResponse.get("status");
                Object fineTunedModel = statusResponse.get("fine_tuned_model");

                log.info("[FineTuning] Current job status: {}", status);

                if ("succeeded".equals(status) && fineTunedModel != null) {
                    log.info("[FineTuning] Final fine-tuned model: {}", fineTunedModel);
                    return fineTunedModel.toString();
                } else if ("failed".equals(status)) {
                    log.error("[FineTuning] Fine-tuning job failed: {}", statusResponse);
                    return null;
                }

                Thread.sleep(10000); // wait 10 seconds before next status check
            }

            log.warn("[FineTuning] Timeout: fine-tuning did not finish within 10 minutes.");
        } catch (Exception e) {
            log.error("[FineTuning] Error while checking job status: {}", e.getMessage());
        }
        return null;
    }
}
