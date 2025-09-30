package com.bigpicture.moonrabbit.domain.dailyquestion.scheduler;

import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyQuestionRequestDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.service.DailyQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyQuestionScheduler {
    private final DailyQuestionService dailyQuestionService;

    // 실제 운영: 매일 자정(서버 시간) 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void createDailyQuestionAtMidnight() {
        DailyQuestionRequestDTO dto = new DailyQuestionRequestDTO(
                "오늘의 질문입니다.",
                java.time.LocalDate.now()
        );
        dailyQuestionService.createOrReplaceForDate(dto);
    }

    // 테스트용: 1분마다 실행 (운영에서는 주석처리)
    @Scheduled(cron = "0 */1 * * * ?")
    public void createDailyQuestionEveryMinuteForTest() {
        DailyQuestionRequestDTO dto = new DailyQuestionRequestDTO(
                "테스트 질문(매분)",
                java.time.LocalDate.now()
        );
        dailyQuestionService.createOrReplaceForDate(dto);
    }
}
