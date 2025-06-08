package com.bigpicture.moonrabbit.global.auth.oauth2.handler;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OAuth2LoginSuccessHandlerController {
    @GetMapping("/oauth2/redirect")
    public ResponseEntity<?> handleRedirect(@RequestParam("token") String token) {
        // token 받아서 처리
        return ResponseEntity.ok(Map.of("token", token));
    }
}
