package com.bigpicture.moonrabbit.domain.report.service;

import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.answer.repository.AnswerRepository;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.report.dto.ReportRequestDTO;
import com.bigpicture.moonrabbit.domain.report.dto.ReportResponseDTO;
import com.bigpicture.moonrabbit.domain.report.entity.Report;
import com.bigpicture.moonrabbit.domain.report.entity.ReportTargetType;
import com.bigpicture.moonrabbit.domain.report.repository.ReportRepository;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final AnswerRepository answerRepository;

    public ReportResponseDTO createReport(User reporter, ReportRequestDTO dto){

        Long targetOwnerId;

        // 신고 대상 확인
        if(dto.getTargetType() == ReportTargetType.BOARD){
            Board board = boardRepository.findById(dto.getTargetId())
                    .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
            targetOwnerId = board.getUser().getId(); // 게시글 작성자 ID
        } else if(dto.getTargetType() == ReportTargetType.ANSWER){
            Answer answer = answerRepository.findById(dto.getTargetId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
            targetOwnerId = answer.getUser().getId(); // 댓글 작성자 ID
        } else {
            throw new CustomException(ErrorCode.INVALID_TARGET_TYPE);
        }

        // 자기 글 신고 금지
        if (reporter.getId().equals(targetOwnerId)) {
            throw new CustomException(ErrorCode.CANNOT_REPORT_OWN);
        }

        // 중복 신고 체크
        reportRepository.findByReporterIdAndTargetTypeAndTargetId(
                reporter.getId(),
                dto.getTargetType(),
                dto.getTargetId()
        ).ifPresent(r -> {
            throw new CustomException(ErrorCode.ALREADY_REPORTED);
        });

        Report report = new Report();
        report.setTargetType(dto.getTargetType());
        report.setTargetId(dto.getTargetId());
        report.setReason(dto.getReason());
        report.setReporter(reporter);

        // targetContent 채우기
        if(dto.getTargetType() == ReportTargetType.BOARD){
            Board board = boardRepository.findById(dto.getTargetId())
                    .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
            report.setTargetContent(board.getContent());
        } else if(dto.getTargetType() == ReportTargetType.ANSWER){
            Answer answer = answerRepository.findById(dto.getTargetId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
            report.setTargetContent(answer.getContent());
        }

        Report saved =  reportRepository.save(report);

        return new ReportResponseDTO(saved);
    }

    // 신고 목록 조회 (타입별, 페이징)
    public Page<ReportResponseDTO> getReportsByType(ReportTargetType type, Pageable pageable){
        return reportRepository.findByTargetType(type, pageable)
                .map(ReportResponseDTO::new);
    }

    // 특정 대상 신고 조회 (페이징)
    public Page<ReportResponseDTO> getReportsByTarget(ReportTargetType type, Long targetId, Pageable pageable){
        return reportRepository.findByTargetTypeAndTargetId(type, targetId, pageable)
                .map(ReportResponseDTO::new);
    }
}
