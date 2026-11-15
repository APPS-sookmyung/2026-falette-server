package com.falette.backend.common.exception;

/** 모든 에러 응답의 공통 형태: { "status": 401, "code": "UNAUTHORIZED", "message": "..." } */
public record ErrorResponse(int status, String code, String message) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus().value(), errorCode.name(), errorCode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.getStatus().value(), errorCode.name(), message);
    }
}
