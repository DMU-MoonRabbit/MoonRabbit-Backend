package com.bigpicture.moonrabbit.global.auth.kakao.service;

import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.auth.kakao.dto.KakaoUserResponseDTO;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

@Service
public class KakaoAuthService {

    private final UserRepository userRepository;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.token-url}")
    private String tokenUrl;

    @Value("${oauth.kakao.user-info-url}")
    private String userInfoUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 인증 코드로 액세스 토큰을 가져오는 메서드
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    // 액세스 토큰으로 사용자 정보를 가져오는 메서드
    public KakaoUserResponseDTO getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");


        // 카카오 정보로 새로운 사용자 생성
        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        // 이메일로 사용자를 조회
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;

        if (optionalUser.isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        } else {
            // 사용자 정보가 없으면 새로 등록
            user = new User();
            user.setEmail(email);
            user.setNickname(nickname);
            user.setProfileImg(profileImageUrl);
            user.setPassword("kakao_login");  // 카카오 로그인에 비밀번호는 필요 없으므로 임의로 설정
            user.setRole("USER");  // 기본 역할은 "USER"
            userRepository.save(user);
        }

        // KakaoUserResponseDTO 반환
        return new KakaoUserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImg()
        );
    }
}
