package com.bigpicture.moonrabbit.domain.user.repository;

import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 유저 찾기 ( 아이디 찾기 )
    Optional<User> findByEmail(String email);

    // totalPoint 기준 내림차순, Pageable 적용
    Page<User> findAllByOrderByTotalPointDesc(Pageable pageable);

    // trustPoint 기준 랭킹
    Page<User> findAllByOrderByTrustPointDesc(Pageable pageable);
}
