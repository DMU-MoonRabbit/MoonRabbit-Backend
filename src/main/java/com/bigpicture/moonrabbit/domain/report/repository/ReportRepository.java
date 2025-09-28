package com.bigpicture.moonrabbit.domain.report.repository;

import com.bigpicture.moonrabbit.domain.report.entity.Report;
import com.bigpicture.moonrabbit.domain.report.entity.ReportTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report,Long> {
    // 특정 타입(BOARD, COMMENT) 신고 목록 조회
    @EntityGraph(attributePaths = {"board", "answer"})
    Page<Report> findByTargetType(ReportTargetType targetType, Pageable pageable);

    // 특정 대상(targetId)에 대한 모든 신고 조회
    @EntityGraph(attributePaths = {"board", "answer"})
    Page<Report> findByTargetTypeAndTargetId(ReportTargetType targetType, Long targetId, Pageable pageable);

    // 중복 신고 방지: 같은 유저가 같은 대상에 대해 신고했는지 확인
    Optional<Report> findByReporterIdAndTargetTypeAndTargetId(Long reporterId, ReportTargetType targetType, Long targetId);
}
