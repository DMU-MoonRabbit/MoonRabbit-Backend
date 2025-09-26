package com.bigpicture.moonrabbit.domain.like.service;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.answer.repository.AnswerRepository;
import com.bigpicture.moonrabbit.domain.like.entity.Likes;
import com.bigpicture.moonrabbit.domain.like.repository.LikesRepository;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final AnswerRepository answerRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;

    // 좋아요
    @Override
    public void likeAnswer(Long answerId, Long userId) {
        if (likesRepository.findByAnswerIdAndUserId(answerId, userId).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_LIKE);
        }
        Answer answer = answerRepository.findById(answerId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Likes like = Likes.builder()
                .answer(answer)
                .user(user)
                .build();
        likesRepository.save(like);
    }


    // 좋아요 취소
    @Override
    @Transactional
    public void unlikeAnswer(Long answerId, Long userId) {
        likesRepository.deleteByAnswerIdAndUserId(answerId, userId);
    }

    @Override
    public AnswerResponseDTO getAnswerDto(Long answerId, Long currentUserId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        return new AnswerResponseDTO(answer, currentUserId);
    }
}
