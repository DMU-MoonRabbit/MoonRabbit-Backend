package com.bigpicture.moonrabbit.global.auth.oauth2.handler;

import com.bigpicture.moonrabbit.global.auth.jwt.provider.JwtProvider;
import com.bigpicture.moonrabbit.global.auth.oauth2.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = (String) oAuth2User.getAttributes().get("email");
        String role = oAuth2User.getAuthorities().iterator().next().getAuthority();
         if (email == null) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        if (kakaoAccount != null) {
            email = (String) kakaoAccount.get("email");  // 카카오에서 이메일을 가져오는 부분
            }
        }
        // JWT 토큰 생성
        String token = jwtProvider.createToken(email, role);

        // 토큰을 쿼리 파라미터로 리다이렉트
        String redirectUri = "https://moonrabbit-web.kro.kr/loading?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        log.info("OAuth2 login success. Redirecting to: {}", redirectUri);
        log.info("OAuth2 Email: {}", email);
        log.info("OAuth2 User: ", oAuth2User);
        response.sendRedirect(redirectUri);
    }
}
