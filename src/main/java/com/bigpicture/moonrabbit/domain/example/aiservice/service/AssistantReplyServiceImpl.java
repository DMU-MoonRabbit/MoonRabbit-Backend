package com.bigpicture.moonrabbit.domain.example.aiservice.service;

import com.bigpicture.moonrabbit.domain.example.aiservice.entity.AssistantReply;
import com.bigpicture.moonrabbit.domain.example.aiservice.repository.AssistantReplyRepository;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class AssistantReplyServiceImpl implements AssistantReplyService {

    private final AssistantReplyRepository replyRepository;

    @Override
    public AssistantReply getReplyByBoardId(Long boardId) {
        return replyRepository.findByBoardId(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ASSISTANT_CATEGORY));
    }

    @Override
    public AssistantReply saveReply(AssistantReply reply) {
        return replyRepository.save(reply);
    }

}
