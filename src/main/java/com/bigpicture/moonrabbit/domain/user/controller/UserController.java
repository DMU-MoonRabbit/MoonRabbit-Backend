package com.bigpicture.moonrabbit.domain.user.controller;


import com.bigpicture.moonrabbit.domain.image.entity.FileType;
import com.bigpicture.moonrabbit.domain.image.service.S3Service;
import com.bigpicture.moonrabbit.domain.user.dto.*;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.global.auth.jwt.dto.JwtDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final S3Service s3Service;


    @GetMapping("/api/admin/debug")
    public String debug(Authentication authentication) {
        return "Authorities: " + authentication.getAuthorities();
    }
    @GetMapping("/kakao")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/kakao");
    }

    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/register")
    public String register() {
        return "/register";
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

    // 사용자 정보 반환 (익명)
    @GetMapping("/nicknames/random")
    @Operation(summary = "랜덤 닉네임 생성 API", description = "로그인된 사용자의 정보를 불러오지만 닉네임을 랜덤닉네임으로 불러옴")
    public ResponseEntity<UserResponseDTO> getRandomNickname() {
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setNickname(userService.generateNickname());
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @GetMapping("/total-point/rank")
    @Operation(summary = "누적 포인트(레벨) 랭킹", description = "누적 포인트(레벨)순으로 조회")
    public Page<UserRankingDTO> getRanking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getTotalPointRanking(page, size);
    }

    @GetMapping("/trust-point/rank")
    @Operation(summary = "신뢰도 랭킹", description = "신뢰도 포인트순으로 조회")
    public Page<UserRankingDTO> getTrustPointRanking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getTrustPointRanking(page, size);
    }
    @Operation(summary = "닉네임 변경", description = "로그인된 사용자의 닉네임을 변경합니다.")
    @PatchMapping("/profile/nickname")
    public ResponseEntity<UserResponseDTO> updateNickname(@Valid @RequestBody UpdateNicknameRequestDTO requestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDTO responseDTO = userService.updateNickname(email, requestDTO.getNewNickname());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 변경", description = "로그인된 사용자의 비밀번호를 변경합니다.")
    @PatchMapping("/profile/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UpdatePasswordRequestDTO requestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updatePassword(
                email,
                requestDTO.getCurrentPassword(),
                requestDTO.getNewPassword(),
                requestDTO.getNewPasswordConfirm()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "프로필 이미지 변경/업로드", description = "S3에 프로필 이미지를 업로드하고 유저 정보에 반영합니다.")
    @PostMapping("/profile/image")
    public ResponseEntity<UserResponseDTO> updateProfileImage(
            @RequestParam("file") MultipartFile file
    ) {
        // 1. 현재 사용자 이메일 및 User 엔티티 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userService.getUserByEmail(email);

        String oldUrl = currentUser.getProfileImg(); // DB에 저장된 현재 프로필 이미지 URL 가져오기

        // 2. S3에 새 파일 업로드 (이전 파일이 있다면 삭제 처리 포함)
        // S3Service의 updateFile을 사용하여 기존 이미지를 삭제하고 새 이미지를 업로드
        // FileType.PROFILE은 "user-profile/" 접두사를 사용합니다.
        String newImageUrl = s3Service.updateFile(oldUrl, file, FileType.PROFILE);

        // 3. UserService를 통해 DB의 프로필 이미지 URL 업데이트
        UserResponseDTO responseDTO = userService.updateProfileImage(email, newImageUrl);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
