package com.bigpicture.moonrabbit.domain.like.service;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;

public interface LikeService {

    void likeAnswer(Long answerId, Long userId);

    void unlikeAnswer(Long answerId, Long userId);

    AnswerResponseDTO getAnswerDto(Long answerId, Long currentUserId);
}
