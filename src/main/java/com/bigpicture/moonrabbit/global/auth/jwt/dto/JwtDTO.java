package com.bigpicture.moonrabbit.global.auth.jwt.dto;

import lombok.Builder;

@Builder
public record JwtDTO(String accessToken, String refreshToken) {

}
