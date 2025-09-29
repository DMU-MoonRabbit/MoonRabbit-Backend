package com.bigpicture.moonrabbit.domain.report.service;

import com.bigpicture.moonrabbit.domain.report.dto.ReportRequestDTO;
import com.bigpicture.moonrabbit.domain.report.dto.ReportResponseDTO;
import com.bigpicture.moonrabbit.domain.report.entity.ReportTargetType;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    ReportResponseDTO createReport(User reporter, ReportRequestDTO dto);

    Page<ReportResponseDTO> getReportsByType(ReportTargetType type, Pageable pageable);

    Page<ReportResponseDTO> getReportsByTarget(ReportTargetType type, Long targetId, Pageable pageable);
}
