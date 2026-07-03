package com.brewtifulsip.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 애플리케이션 공통 에러 코드. 매직 문자열 대신 Enum으로 관리(CLAUDE.md 5장).
 * HTTP 상태와 기본 메시지를 함께 보유한다.
 */
public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    INVALID_BEAN_SELECTION(HttpStatus.BAD_REQUEST, "원두 선택이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    INVALID_ORDER_STATE_TRANSITION(HttpStatus.CONFLICT, "허용되지 않는 주문 상태 전이입니다."),
    REVIEW_NOT_ALLOWED(HttpStatus.CONFLICT, "리뷰를 작성할 수 없습니다.");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
