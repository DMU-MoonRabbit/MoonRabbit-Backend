package com.bigpicture.moonrabbit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 사용자 관련 에러
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U001", "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "해당 사용자를 찾을 수 없습니다."),

    // 입력값 검증 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "V001", "잘못된 입력 값입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "V002", "이메일 혹은 비밀번호가 유효하지 않습니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
