package com.bigpicture.moonrabbit.domain.user.controller;


import com.bigpicture.moonrabbit.domain.user.dto.LoginRequestDTO;
import com.bigpicture.moonrabbit.domain.user.dto.UserRequestDTO;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.dto.UserResponseDTO;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.global.auth.jwt.dto.JwtDTO;
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


    // 유저 저장
    @PostMapping("/save")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO responseDTO = userService.saveUser(userRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 이메일로 유저 찾기
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        JwtDTO jwtDTO = userService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        return new ResponseEntity<>(jwtDTO, HttpStatus.OK);
    }

    // 사용자 정보 반환
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile() {
        // 로그인된 사용자의 정보를 반환하는 예시
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(new UserResponseDTO(user));
    }
}
