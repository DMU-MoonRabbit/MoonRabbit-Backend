package com.bigpicture.moonrabbit.domain.report.dto;

import com.bigpicture.moonrabbit.domain.report.entity.Report;
import com.bigpicture.moonrabbit.domain.report.entity.ReportStatus;
import com.bigpicture.moonrabbit.domain.report.entity.ReportTargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportResponseDTO {
    private Long id;
    private ReportTargetType reportTargetType;
    private Long targetId;
    private String targetContent; // 게시글/댓글 내용 추가
    private String reason;
    private Long reporterId;
    private ReportStatus status;

    // Report 엔티티 기반 생성자
    public ReportResponseDTO(Report report) {
        this.id = report.getId();
        this.reportTargetType = report.getTargetType();
        this.targetId = report.getTargetId();
        this.reason = report.getReason();
        this.reporterId = report.getReporter().getId();
        this.targetContent = report.getTargetContent();
        this.status = report.getStatus();
    }
}

