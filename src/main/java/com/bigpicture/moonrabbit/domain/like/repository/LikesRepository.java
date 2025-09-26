package com.bigpicture.moonrabbit.domain.like.repository;

import com.bigpicture.moonrabbit.domain.like.entity.Likes;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByAnswerIdAndUserId(Long answerId, Long userId);
    void deleteByAnswerIdAndUserId(Long answerId, Long userId);
}
