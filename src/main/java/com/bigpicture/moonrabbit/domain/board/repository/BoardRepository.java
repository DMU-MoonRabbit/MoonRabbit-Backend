package com.bigpicture.moonrabbit.domain.board.repository;

import com.bigpicture.moonrabbit.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findWithCommentsById(Long id);

    Page<Board> findByUser_Id(Long userId, Pageable pageable);

    // 유저 ID로 페이징 없이 전체 조회 (최신순 정렬)
    List<Board> findByUser_IdOrderByCreatedAtDesc(Long userId);

    // 카테고리별 페이징 조회를 위한 메서드 추가
    Page<Board> findByCategory(String category, Pageable pageable);
}
