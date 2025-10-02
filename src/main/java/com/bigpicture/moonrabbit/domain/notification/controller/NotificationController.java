package com.bigpicture.moonrabbit.domain.notification.controller;

import com.bigpicture.moonrabbit.domain.notification.dto.NotificationResponseDTO;
import com.bigpicture.moonrabbit.domain.notification.entity.Notification;
import com.bigpicture.moonrabbit.domain.notification.service.NotificationService;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Operation(summary = "실시간 알림 구독", description = "SSE 방식으로 실시간 알림 수신을 위한 구독을 생성합니다.")
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // ← subject(email)가 들어 있음

        // 이메일로 userId 조회
        Long userId = userService.getUserIdByEmail(email);

        return notificationService.createEmitter(userId);
    }

    // 알림 목록 조회
    @Operation(summary = "알림 목록 조회", description = "로그인한 사용자의 모든 알림을 조회합니다.")
    @GetMapping
    public List<NotificationResponseDTO> getNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // ← subject(email)가 들어 있음

        // 이메일로 userId 조회
        User user = userRepository.findById(userService.getUserIdByEmail(email))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return notificationService.getNotifications(user);
    }

    // 알림 읽음 처리
    @Operation(summary = "알림 읽음 처리", description = "알림 ID를 지정하여 해당 알림을 읽음 처리합니다.")
    @PatchMapping("/{id}/read") // PATCH가 적합, 일부 속성(isRead)만 수정
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id); // 서비스에서 해당 알림 isRead = true 처리
        return ResponseEntity.ok().build(); // 상태 코드 200 반환
    }



}
