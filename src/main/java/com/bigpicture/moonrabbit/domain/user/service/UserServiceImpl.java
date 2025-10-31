package com.bigpicture.moonrabbit.domain.user.service;

import com.bigpicture.moonrabbit.domain.item.dto.EquippedItemDTO;
import com.bigpicture.moonrabbit.domain.item.service.UserItemService;
import com.bigpicture.moonrabbit.domain.sms.entity.Sms;
import com.bigpicture.moonrabbit.domain.sms.repository.SmsRepository;
import com.bigpicture.moonrabbit.domain.user.dto.UserRankingDTO;
import com.bigpicture.moonrabbit.domain.user.dto.UserRequestDTO;
import com.bigpicture.moonrabbit.domain.user.dto.UserResponseDTO;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.auth.jwt.dto.JwtDTO;
import com.bigpicture.moonrabbit.global.auth.jwt.generator.JwtGenerator;
import com.bigpicture.moonrabbit.global.auth.jwt.provider.JwtProvider;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;


@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 확인용
    private final JwtGenerator jwtGenerator; // JWT 생성기
    private final JwtProvider jwtTokenProvider; // Refresh Token 유효성 검증을 위해 필드 추가
    private final SmsRepository smsRepository;
    private final UserItemService userItemService;

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
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    @Override
    @Transactional
    public JwtDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.USER_NOT_FOUND)); // 사용자 존재 여부 확인
        if(!(user.getProvider().equals("common"))) {
            throw new CustomException(ErrorCode.USER_OTHER_PROVIDER);
        }
        // 비밀번호 확인 (저장된 비밀번호와 입력받은 비밀번호 비교)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD); // 비밀번호 불일치 시 예외 발생
        }
        // JWT 생성 (Access: 5분, Refresh: 14일)
        JwtDTO jwtDTO = jwtGenerator.generateToken(user.getEmail(), user.getRole());

        // Refresh Token 저장
        user.updateRefreshToken(jwtDTO.refreshToken());
        userRepository.save(user);

        // 로그인 성공시, 이메일과 권한정보를 같이 넘김
        return jwtDTO;
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
    public Page<UserRankingDTO> getTotalPointRanking(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // 0-based 페이지
        return userRepository.findAllByOrderByTotalPointDesc(pageable)
                .map(user -> {
                    List<EquippedItemDTO> equippedItems = userItemService.getEquippedItems(user.getId()); // 추가
                    return new UserRankingDTO(user, equippedItems); // 수정
                }); // 엔티티 → DTO 변환
    }

    @Override
    // trustPoint 기준
    public Page<UserRankingDTO> getTrustPointRanking(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllByOrderByTrustPointDesc(pageable)
                .map(user -> {
                    List<EquippedItemDTO> equippedItems = userItemService.getEquippedItems(user.getId()); // 추가
                    return new UserRankingDTO(user, equippedItems); // 수정
                });
    }

    @Override
    @Transactional
    public UserResponseDTO updateNickname(String email, String newNickname) {
        User user = getUserByEmail(email);
        user.setNickname(newNickname);
        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public void updatePassword(String email, String currentPassword, String newPassword, String newPasswordConfirm) {
        User user = getUserByEmail(email);

        // 1. 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 2. 새 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (!Objects.equals(newPassword, newPasswordConfirm)) {
            throw new CustomException(ErrorCode.PASSWORD_COFIRM_ERROR);
        }

        // 3. 새 비밀번호를 암호화하여 저장
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    @Override
    @Transactional
    public UserResponseDTO updateProfileImage(String email, String newImageUrl) {
        User user = getUserByEmail(email);
        user.setProfileImg(newImageUrl);
        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public JwtDTO reissueAccessToken(String refreshToken) {
        // 1. Refresh Token 유효성 및 만료 여부 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.WRONG_ACCESS);
        }

        // 2. DB에서 Refresh Token을 가진 사용자 찾기
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 3. 새로운 Access Token 및 Refresh Token 생성
        JwtDTO newJwtDTO = jwtGenerator.generateToken(user.getEmail(), user.getRole());

        // 4. 새 Refresh Token으로 DB 업데이트
        user.updateRefreshToken(newJwtDTO.refreshToken());
        userRepository.save(user);

        return newJwtDTO;
    }
}
