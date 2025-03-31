package com.bigpicture.moonrabbit.global.auth.kakao.controller;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.auth.kakao.service.KakaoAuthService;
import com.bigpicture.moonrabbit.global.auth.kakao.dto.KakaoUserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoAuthController {
    private final KakaoAuthService kakaoAuthService;


    private final UserRepository userRepository;

    public KakaoAuthController(KakaoAuthService kakaoAuthService, UserRepository userRepository) {
        this.kakaoAuthService = kakaoAuthService;
        this.userRepository = userRepository;
    }

    @GetMapping("/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        // 인증 코드로 액세스 토큰을 가져옵니다.
        String accessToken = kakaoAuthService.getAccessToken(code);
        // 액세스 토큰으로 사용자 정보를 가져옵니다.
        KakaoUserResponseDTO userInfo = kakaoAuthService.getUserInfo(accessToken);


        return ResponseEntity.ok("로그인 성공, 사용자 정보 저장 완료");
    }
}