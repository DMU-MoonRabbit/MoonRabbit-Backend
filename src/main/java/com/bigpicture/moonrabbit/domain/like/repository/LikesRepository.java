package com.bigpicture.moonrabbit.domain.like.repository;

import com.bigpicture.moonrabbit.domain.like.entity.Like;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUser(User user);
}
