package com.bigpicture.moonrabbit.domain.example.aiservice.service;

import com.bigpicture.moonrabbit.domain.example.aiservice.entity.AssistantReply;
import com.bigpicture.moonrabbit.domain.example.aiservice.repository.AssistantReplyRepository;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssistantReplyService {

    private final AssistantReplyRepository replyRepository;

    public AssistantReply getReplyByBoardId(Long boardId) {
        return replyRepository.findByBoardId(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ASSISTANT_CATEGORY));
    }

    public AssistantReply saveReply(AssistantReply reply) {
        return replyRepository.save(reply);
    }

}
