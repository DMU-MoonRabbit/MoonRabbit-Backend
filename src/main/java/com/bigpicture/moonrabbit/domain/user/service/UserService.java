package com.bigpicture.moonrabbit.domain.user.service;

import com.bigpicture.moonrabbit.domain.user.dto.UserRequestDTO;
import com.bigpicture.moonrabbit.domain.user.dto.UserResponseDTO;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.auth.jwt.dto.JwtDTO;
import com.bigpicture.moonrabbit.global.auth.jwt.generator.JwtGenerator;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bigpicture.moonrabbit.global.exception.CustomException;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 확인용
    private final JwtGenerator jwtGenerator; // JWT 생성기

    // 유저 저장
    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userRequestDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        if (!Objects.equals(userRequestDTO.getPassword(), userRequestDTO.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_COFIRM_ERROR);
        }
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setNickname(userRequestDTO.getNickname());
        User savedUser = userRepository.save(user);


        return new UserResponseDTO(savedUser);
    }

    // 이메일로 유저 찾기
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    // 이메일과 비밀번호로 로그인
    public JwtDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 사용자 존재 여부 확인

        // 비밀번호 확인 (저장된 비밀번호와 입력받은 비밀번호 비교)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD); // 비밀번호 불일치 시 예외 발생
        }

        // 로그인 성공시, 이메일과 권한정보를 같이 넘김
        return jwtGenerator.generateToken(user.getEmail(), user.getRole());
    }
}
