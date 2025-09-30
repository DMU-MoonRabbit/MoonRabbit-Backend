package com.bigpicture.moonrabbit.domain.dailyquestion.controller;

import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyAnswerHistoryResponseDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyAnswerRequestDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyAnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyQuestionResponseDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyAnswer;
import com.bigpicture.moonrabbit.domain.dailyquestion.entity.DailyQuestion;
import com.bigpicture.moonrabbit.domain.dailyquestion.service.DailyAnswerService;
import com.bigpicture.moonrabbit.domain.dailyquestion.service.DailyQuestionService;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/daily")
@RequiredArgsConstructor
public class DailyController {
    private final DailyQuestionService dailyQuestionService;
    private final DailyAnswerService dailyAnswerService;
    private final UserService userService;

    // 오늘의 질문 조회 (익명 접근 허용하면 인증 없어도)
    @Operation(summary = "오늘의 질문 조회", description = "익명 접근 가능. 오늘의 DailyQuestion을 조회합니다.")
    @GetMapping("/question")
    public ResponseEntity<DailyQuestionResponseDTO> getTodayQuestion() {
        DailyQuestion q = dailyQuestionService.getToday();
        if (q == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new DailyQuestionResponseDTO(q.getId(), q.getDate(), q.getContent()));
    }

    @Operation(summary = "오늘 질문에 답변 제출/수정", description = "인증 필요. DailyAnswer를 새로 제출하거나 기존 답변을 수정합니다.")
    @PostMapping("/answer")
    public ResponseEntity<DailyAnswerResponseDTO> submitAnswer(@RequestBody DailyAnswerRequestDTO request) {
        DailyAnswerResponseDTO dto = dailyAnswerService.submitOrUpdateTodayAnswer(request.getAnswer());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "오늘 내가 쓴 답변 조회", description = "인증 필요. 사용자가 오늘 작성한 답변을 조회합니다.")
    @GetMapping("/answer/me")
    public ResponseEntity<DailyAnswerResponseDTO> getMyTodayAnswer() {
        DailyAnswerResponseDTO dto = dailyAnswerService.getMyTodayAnswer();
        if (dto == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "내 답변 히스토리 조회", description = "인증 필요. 페이징 가능하며 사용자가 작성한 DailyAnswer 리스트를 반환합니다.")
    @GetMapping("/history")
    public Page<DailyAnswerResponseDTO> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.getUserByEmail(email);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<DailyAnswer> answers = dailyAnswerService.getMyAnswerHistory(currentUser, pageable);

        return answers.map(answer ->
                new DailyAnswerResponseDTO(
                        answer.getId(),
                        answer.getDailyQuestion().getId(),
                        answer.getDailyQuestion().getContent(),
                        answer.getAnswer(),
                        answer.getCreatedAt()
                ));
    }
}
