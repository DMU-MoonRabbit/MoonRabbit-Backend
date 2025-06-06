package com.bigpicture.moonrabbit.domain.like.repository;

import com.bigpicture.moonrabbit.domain.like.entity.Likes;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByUser(User user);
}
