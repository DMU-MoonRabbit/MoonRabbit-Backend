package com.bigpicture.moonrabbit.domain.user.controller;


import com.bigpicture.moonrabbit.domain.user.dto.LoginRequestDTO;
import com.bigpicture.moonrabbit.domain.user.dto.UserRequestDTO;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.dto.UserResponseDTO;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.global.auth.jwt.dto.JwtDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/register")
    public String register() {
        return "/register";
    }

    @GetMapping("/profile")
    public String profile() {
        return "/profile";
    }

    @GetMapping("/email")
    public String email() {
        return "/email";
    }

    // 유저 저장
    @Operation(summary = "유저 생성", description = "이메일, 닉네임, 비밀번호, 비밀번호 확인, 전화번호, 전화번호 인증 정보로 유저를 생성")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {

        UserResponseDTO responseDTO = userService.saveUser(userRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 이메일로 유저 찾기
    @Operation(summary = "유저 생성", description = "이메일을 통하여 유저정보를 찾음")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    // 로그인 API
    @Operation(summary = "로그인", description = "로그인시 토큰정보를 반환함")
    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        JwtDTO jwtDTO = userService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        return new ResponseEntity<>(jwtDTO, HttpStatus.OK);
    }

    // 사용자 정보 반환
    @GetMapping("/profile")
    @Operation(summary = "유저 정보", description = "로그인된 사용자가 본인의 정보를 확인할 수 있음")
    public ResponseEntity<UserResponseDTO> getUserProfile() {
        // 로그인된 사용자의 정보를 반환하는 예시
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(new UserResponseDTO(user));
    }
}
