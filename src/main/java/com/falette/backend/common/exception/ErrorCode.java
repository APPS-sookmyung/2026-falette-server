package com.falette.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 세션 또는 X-Guest-UUID 헤더가 필요합니다."),
    INVALID_GUEST_UUID(HttpStatus.BAD_REQUEST, "X-Guest-UUID 형식이 올바르지 않습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    READING_NOT_FOUND(HttpStatus.NOT_FOUND, "리딩 기록을 찾을 수 없습니다."),
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "고민 질문을 찾을 수 없습니다."),
    INVALID_TAROT_CARD(HttpStatus.BAD_REQUEST, "존재하지 않는 타로 카드입니다."),
    INVALID_CONCERN_OPTION(HttpStatus.BAD_REQUEST, "선택한 고민 옵션이 올바르지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "요청 Content-Type은 application/json이어야 합니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
