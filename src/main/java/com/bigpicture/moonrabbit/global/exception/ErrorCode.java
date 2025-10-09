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

    // 게시글 관련 에러
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "B001", "해당 게시글을 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.BAD_REQUEST,"B002" ,"글쓴이만 채택이 가능합니다." ),

    // 댓글 관련 에러
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "해당 댓글을 찾을 수 없습니다."),
    INVALID_ANSWER(HttpStatus.BAD_REQUEST,"A002" ,"유효하지 않은 댓글입니다." ),
    UNAUTHORIZED_ACTION(HttpStatus.BAD_REQUEST,"A003","글쓴이만 가능합니다." ),
    CANNOT_SELECT_OWN_COMMENT(HttpStatus.BAD_REQUEST,"A004","본인의 답변을 채택할 수 없습니다."),
    ALREADY_SELECTED_ANSWER(HttpStatus.BAD_REQUEST,"A005","이미 채택된 답변이 존재합니다."),
    
    // 아이템 관련 에러
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST,"I001","아이템을 찾을 수 없습니다."),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST,"I002","아이템을 구매하기 위한 포인트가 부족합니다."),
    ALREADY_OWNED_ITEM(HttpStatus.BAD_REQUEST,"I003","해당 아이템을 이미 보유중입니다."),

    // 일일질문 관련 에러
    QUESTION_NOT_FOUND(HttpStatus.BAD_REQUEST,"Q001","오늘의 질문이 존재하지 않습니다."),
    QUESTION_ANSWER_NOT_FOUND(HttpStatus.BAD_REQUEST,"Q001","답변이 존재하지 않습니다."),
    // 신고 관련 에러
    ALREADY_REPORTED(HttpStatus.BAD_REQUEST, "R001" ,"이미 신고한 대상입니다." ),
    INVALID_TARGET_TYPE(HttpStatus.BAD_REQUEST,"R002" ,"신고 유형이 올바르지 않습니다." ),
    CANNOT_REPORT_OWN(HttpStatus.BAD_REQUEST,"R003" ,"본인을 신고할 수 없습니다." ),

    // AI 답변 관련 에러 추가
    AI_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "AI001", "AI 답변을 찾을 수 없습니다."),
    INVALID_ASSISTANT_CATEGORY(HttpStatus.BAD_REQUEST, "AI002", "지원하지 않는 카테고리입니다."),

    // 입력값 검증 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "V001", "잘못된 입력 값입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "V002", "이메일 혹은 비밀번호가 유효하지 않습니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 오류가 발생했습니다."),
    WRONG_ACCESS(HttpStatus.BAD_REQUEST, "S002", "잘못된 접근입니다."),

    // 좋아요 관련 에러
    DUPLICATE_LIKE(HttpStatus.BAD_REQUEST, "L001", "이미 좋아요를 눌렀습니다."),
    ALREADY_LIKED_BOARD(HttpStatus.BAD_REQUEST, "L002" , "이미 북마크가 되어있는 고민입니다." ),

    // SMS 관련 에러
    SMS_CERTIFICATION_FAILED(HttpStatus.BAD_REQUEST, "P003", "인증 에러가 발생했습니다."),

    // S3 이미지 관련 에러
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "IMG001", "이미지 업로드에 실패했습니다."),
    IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "IMG002", "이미지 삭제에 실패했습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "IMG003", "지원하지 않는 파일 타입입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}