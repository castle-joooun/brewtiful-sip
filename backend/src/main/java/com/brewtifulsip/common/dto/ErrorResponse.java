package com.brewtifulsip.common.dto;

import com.brewtifulsip.common.exception.ErrorCode;

import java.time.OffsetDateTime;

/**
 * 전역 공통 에러 응답 포맷. docs/api-docs/openapi.yaml 의 ErrorResponse 스키마와 대응.
 */
public record ErrorResponse(String code, String message, OffsetDateTime timestamp) {

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.name(), message, OffsetDateTime.now());
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, OffsetDateTime.now());
    }
}
