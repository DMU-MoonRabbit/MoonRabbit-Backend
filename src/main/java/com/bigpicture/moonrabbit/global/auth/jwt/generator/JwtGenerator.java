package com.bigpicture.moonrabbit.global.auth.jwt.generator;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.bigpicture.moonrabbit.global.auth.jwt.dto.JwtDTO;
import java.security.Key;
import java.util.Date;

@Component
public class JwtGenerator {
    private final Key key;

    // application.properties에서 secret 값 가져와서 key에 저장
    public JwtGenerator(@Value("${jwt.secret}") String secretKey){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // 토큰 생성 메서드
    public JwtDTO generateToken(String id, String role) {

        long now = (new Date()).getTime();

        // AccessToken 생성 (5분)
        Date accessTokenExpiresIn = new Date(now + 300000);

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("auth", role) // 권한 정보 추가
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //Refresh Token 생성 (14일)
        String refreshToken = Jwts.builder()
                .setSubject(id)
                .setExpiration(new Date(now + 1209600000L))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();


        return new JwtDTO(accessToken, refreshToken);
    }

}
