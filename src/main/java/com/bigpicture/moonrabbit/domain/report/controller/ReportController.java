package com.bigpicture.moonrabbit.domain.report.controller;

import com.bigpicture.moonrabbit.domain.report.dto.ReportRequestDTO;
import com.bigpicture.moonrabbit.domain.report.dto.ReportResponseDTO;
import com.bigpicture.moonrabbit.domain.report.entity.ReportTargetType;
import com.bigpicture.moonrabbit.domain.report.service.ReportService;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    @Operation(summary = "신고 생성")
    @PostMapping("api/create")
    public ResponseEntity<ReportResponseDTO> createReport(@RequestBody ReportRequestDTO dto) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // 이메일(subject)

        // 이메일로 User 엔티티 조회
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ReportResponseDTO response = reportService.createReport(currentUser, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "타입별 신고 목록 조회 (페이징)")
    @GetMapping("/list")
    public ResponseEntity<Page<ReportResponseDTO>> getReportsByType(@RequestParam ReportTargetType type,
                                                                    @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                                                    Pageable pageable){
        Page<ReportResponseDTO> reports = reportService.getReportsByType(type, pageable);
        return ResponseEntity.ok(reports);
    }

    @Operation(summary = "특정 대상 신고 조회 (페이징)")
    @GetMapping("/{targetId}")
    public ResponseEntity<Page<ReportResponseDTO>> getReportsByTarget(
            @PathVariable Long targetId,
            @RequestParam ReportTargetType type,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        Page<ReportResponseDTO> reports = reportService.getReportsByTarget(type, targetId, pageable);
        return ResponseEntity.ok(reports);
    }
}
