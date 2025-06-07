package com.bigpicture.moonrabbit.domain.example.aiservice.repository;

import com.bigpicture.moonrabbit.domain.example.aiservice.entity.AssistantReply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AssistantReplyRepository extends JpaRepository<AssistantReply, Long> {
    Optional<AssistantReply> findByBoardId(Long boardId);
}