package com.bigpicture.moonrabbit.domain.example.aiservice.service;

import com.bigpicture.moonrabbit.domain.example.aiservice.entity.AssistantReply;

public interface AssistantReplyService {
    AssistantReply getReplyByBoardId(Long boardId);

    AssistantReply saveReply(AssistantReply reply);
}
