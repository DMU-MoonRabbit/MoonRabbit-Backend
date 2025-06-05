package com.bigpicture.moonrabbit.domain.board.repository;

import com.bigpicture.moonrabbit.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findWithCommentsById(Long id);
}
