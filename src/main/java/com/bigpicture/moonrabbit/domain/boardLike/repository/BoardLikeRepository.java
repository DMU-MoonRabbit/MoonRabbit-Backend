package com.bigpicture.moonrabbit.domain.boardLike.repository;

import com.bigpicture.moonrabbit.domain.boardLike.entity.BoardLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
    void deleteByBoardIdAndUserId(Long boardId, Long userId);
    Page<BoardLike> findByUserId(Long userId, Pageable pageable);
}
