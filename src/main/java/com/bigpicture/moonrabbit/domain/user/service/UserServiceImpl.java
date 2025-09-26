package com.bigpicture.moonrabbit.domain.user.service;

import com.bigpicture.moonrabbit.domain.sms.entity.Sms;
import com.bigpicture.moonrabbit.domain.sms.repository.SmsRepository;
import com.bigpicture.moonrabbit.domain.user.dto.UserRankingDTO;
import com.bigpicture.moonrabbit.domain.user.dto.UserRequestDTO;
import com.bigpicture.moonrabbit.domain.user.dto.UserResponseDTO;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.auth.jwt.dto.JwtDTO;
import com.bigpicture.moonrabbit.global.auth.jwt.generator.JwtGenerator;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bigpicture.moonrabbit.global.exception.CustomException;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;


@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 확인용
    private final JwtGenerator jwtGenerator; // JWT 생성기
    private final SmsRepository smsRepository;
    private static final String[] ADJECTIVES = {
            "행복한", "용감한", "느긋한", "귀여운", "기발한", "지혜로운", "빛나는", "엉뚱한", "차분한"
    };

    private static final String[] NOUNS = {
            "토끼", "호랑이", "고양이", "사자", "판다", "부엉이", "돌고래", "햄스터", "고슴도치"
    };

    // 유저 저장
    @Override
    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userRequestDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        if (!Objects.equals(userRequestDTO.getPassword(), userRequestDTO.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_COFIRM_ERROR);
        }

        // SMS 인증 검증 추가
        Sms sms = smsRepository.findByPhone(userRequestDTO.getPhoneNum());
        if (sms == null || !sms.getCertification().equals(userRequestDTO.getVerification())) {
            throw new CustomException(ErrorCode.SMS_CERTIFICATION_FAILED);  // 새로운 에러코드 정의 필요
        }

        // SMS 인증 성공 시 DB에서 인증정보 삭제
        smsRepository.delete(sms);
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        if(userRequestDTO.getNickname() == null) {
            user.setNickname(generateNickname());
        }

        User savedUser = userRepository.save(user);


        return new UserResponseDTO(savedUser);
    }

    // 이메일로 유저 찾기
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    @Override
    // 이메일과 비밀번호로 로그인
    public JwtDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 사용자 존재 여부 확인
        if(!(user.getProvider().equals("common"))) {
            throw new CustomException(ErrorCode.USER_OTHER_PROVIDER);
        }
        // 비밀번호 확인 (저장된 비밀번호와 입력받은 비밀번호 비교)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD); // 비밀번호 불일치 시 예외 발생
        }

        // 로그인 성공시, 이메일과 권한정보를 같이 넘김
        return jwtGenerator.generateToken(user.getEmail(), user.getRole());
    }

    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }

    @Override
    // 랜덤 닉네임 생성
    public String generateNickname() {
        Random random = new Random();
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        return adjective + noun;
    }

    @Override
    // 누적 포인트 30점마다 1레벨
    public int calculateLevel(int totalPoint) {
        return totalPoint / 30 + 1;
    }

    @Override
    public Page<UserRankingDTO> getTotalPointRanking(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // 0-based 페이지
        return userRepository.findAllByOrderByTotalPointDesc(pageable)
                .map(UserRankingDTO::new); // 엔티티 → DTO 변환
    }

    @Override
    // trustPoint 기준
    public Page<UserRankingDTO> getTrustPointRanking(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllByOrderByTrustPointDesc(pageable)
                .map(UserRankingDTO::new);
    }


}
