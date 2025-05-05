package com.bigpicture.moonrabbit.domain.board.repository;

import com.bigpicture.moonrabbit.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
