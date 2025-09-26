package com.bigpicture.moonrabbit.domain.user.service;

import com.bigpicture.moonrabbit.domain.user.dto.UserRequestDTO;
import com.bigpicture.moonrabbit.domain.user.dto.UserResponseDTO;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.global.auth.jwt.dto.JwtDTO;

import java.util.Random;

public interface UserService {
    // 유저 저장
    UserResponseDTO saveUser(UserRequestDTO userRequestDTO);

    // 이메일로 유저 찾기
    User getUserByEmail(String email);

    // 이메일과 비밀번호로 로그인
    JwtDTO login(String email, String password);

    // 유저 아이디 이메일로 찾기
    Long getUserIdByEmail(String email);

    // 랜덤 닉네임 생성
    String generateNickname();
}
