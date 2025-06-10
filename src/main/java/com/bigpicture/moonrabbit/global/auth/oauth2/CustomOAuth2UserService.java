package com.bigpicture.moonrabbit.global.auth.oauth2;

import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId(); // ex: "google" or "kakao"

        String providerId;
        String email;
        String name;
        String picture;

        if (provider.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            providerId = (String) kakaoAccount.get("id");
            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");
            picture = (String) profile.get("profile_image_url");

            log.info("[OAuth2-KAKAO] id: {}", providerId);
            log.info("[OAuth2-KAKAO] email: {}", email);
            log.info("[OAuth2-KAKAO] nickname: {}", name);
            log.info("[OAuth2-KAKAO] profile image: {}", picture);
        } else if (provider.equals("google")) {
            providerId = (String) attributes.get("sub");
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            picture = (String) attributes.get("picture");
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth2 제공자입니다: " + provider);
        }
        //System.out.println("attributes = " + attributes);

        User user = userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(User.builder()
                        .email(email)
                        .nickname(name)
                        .profileImg(picture)
                        .provider(provider)
                        .providerId(providerId)
                        .role("USER")
                        .level(1)
                        .build())
        );
        String nameAttributeKey = provider.equals("kakao") ? "id" : "sub";
        return new CustomOAuth2User(user.getRole(), attributes, nameAttributeKey);
    }

}
