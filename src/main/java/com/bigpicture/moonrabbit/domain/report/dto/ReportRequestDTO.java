package com.bigpicture.moonrabbit.domain.report.dto;

import com.bigpicture.moonrabbit.domain.report.entity.ReportTargetType;
import lombok.Getter;

@Getter
public class ReportRequestDTO {
    private ReportTargetType targetType;
    private Long targetId;
    private String reason;
}
