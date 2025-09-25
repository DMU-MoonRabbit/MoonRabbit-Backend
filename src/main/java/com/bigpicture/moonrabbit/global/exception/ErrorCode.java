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
    PASSWORD_COFIRM_ERROR(HttpStatus.BAD_REQUEST, "U003", "입력한 비밀번호가 일치하지 않습니다."),
    USER_INCORRECT(HttpStatus.BAD_REQUEST, "U004", "작성자만 수정이 가능합니다."),
    USER_OTHER_PROVIDER(HttpStatus.BAD_REQUEST, "U005", "이미 소셜 미디어로 연동하여 가입된 계정입니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "해당 댓글을 찾을 수 없습니다."),
    // 게시글 관련 에러
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "B001", "해당 게시글을 찾을 수 없습니다."),

    // AI 답변 관련 에러 추가
    AI_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "AI001", "AI 답변을 찾을 수 없습니다."),
    INVALID_ASSISTANT_CATEGORY(HttpStatus.BAD_REQUEST, "AI002", "지원하지 않는 카테고리입니다."),

    // 입력값 검증 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "V001", "잘못된 입력 값입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "V002", "이메일 혹은 비밀번호가 유효하지 않습니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 오류가 발생했습니다."),
    WRONG_ACCESS(HttpStatus.BAD_REQUEST, "S002", "잘못된 접근입니다."),

    // SMS 관련 에러
    SMS_CERTIFICATION_FAILED(HttpStatus.BAD_REQUEST, "P003", "인증 에러가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}