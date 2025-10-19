package com.bigpicture.moonrabbit.domain.fine.service;

import com.bigpicture.moonrabbit.domain.fine.entity.FineDataset;
import com.bigpicture.moonrabbit.domain.fine.repository.FineDatasetRepository;
import com.bigpicture.moonrabbit.domain.example.aiservice.StreamingAssistant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineDatasetService {

    private final FineDatasetRepository fineDatasetRepository;
    private final StreamingAssistant streamingAssistant;

    public File createTrainingJsonl(String extractedText, String sourceName) {
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("[INFO] JSONL generation started: " + sourceName);

            String datasetDir = "data/fine-datasets";
            File dir = new File(datasetDir);
            if (!dir.exists()) dir.mkdirs();

            File jsonlFile = new File(dir, "fine_dataset_" + System.currentTimeMillis() + ".jsonl");

            // Split text into chunks to avoid model input overflow
            List<String> chunks = splitText(extractedText, 8000);

            // Merge all chunks into one big text
            String mergedText = String.join("\n\n", chunks);
            System.out.println("[DEBUG] Merged text length: " + mergedText.length());

            // Prompt for LLM
            String prompt = String.format("""
            당신의 임무는 심리상담 문서 전체를 분석하여,
            OpenAI fine-tuning용 JSONL 데이터를 생성하는 것입니다.

            출력 조건:
            - 오직 JSON 형식만 출력하세요.
            - 설명, 문장, 번호, 불릿, 공백, 따옴표 밖 내용은 절대 포함하지 마세요.
            - JSON 객체는 정확히 20개만 생성해야 합니다.
            - 각 줄에는 완전한 JSON 객체 1개만 포함되어야 합니다.
            - JSON은 아래 구조를 반드시 따르세요.

            {"messages":[
              {"role":"system","content":"너는 따뜻하고 공감적인 전문 심리상담가야."},
              {"role":"user","content":"(내담자의 고민 내용)"},
              {"role":"assistant","content":"(공감적으로 반응하고 탐색을 유도하는 상담가의 답변)"}
            ]}

            언어 지침:
            - 모든 내용은 100%% 한국어로 작성합니다.
            - 영어 단어나 번역문을 포함하지 않습니다.
            - JSON 외의 문장은 절대 출력하지 마세요.

            아래의 문서 내용을 참고해 JSONL 20개를 만드세요.

            --- 문서 내용 시작 ---
            %s
            --- 문서 내용 끝 ---
            """, mergedText);

            // Call AI once (sync version)
            System.out.println("[DEBUG] streamingAssistant.chat started");
            String aiResponse = streamingAssistant.chat(prompt);

            // Filter JSON lines only
            String filtered = aiResponse.lines()
                    .filter(line -> line.trim().startsWith("{") && line.trim().endsWith("}"))
                    .collect(Collectors.joining("\n"));

            // Save file
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(jsonlFile), StandardCharsets.UTF_8)) {
                writer.write(filtered);
            }

            // Save DB record
            FineDataset dataset = new FineDataset();
            dataset.setFileName(jsonlFile.getAbsolutePath());
            dataset.setSourceFile(sourceName);
            fineDatasetRepository.save(dataset);

            long endTime = System.currentTimeMillis();
            System.out.println("[INFO] JSONL generation completed: " + jsonlFile.getName() +
                    " (elapsed: " + (endTime - startTime) / 1000.0 + "s)");

            return jsonlFile;

        } catch (Exception e) {
            throw new RuntimeException("JSONL generation failed: " + e.getMessage(), e);
        }
    }

    private List<String> splitText(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        int length = text.length();
        for (int i = 0; i < length; i += chunkSize) {
            int end = Math.min(length, i + chunkSize);
            chunks.add(text.substring(i, end));
        }
        return chunks;
    }
}
