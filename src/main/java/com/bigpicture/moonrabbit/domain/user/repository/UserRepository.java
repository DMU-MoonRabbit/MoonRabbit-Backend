package com.bigpicture.moonrabbit.domain.user.repository;

import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 유저 찾기 ( 아이디 찾기 )
    Optional<User> findByEmail(String email);
}
