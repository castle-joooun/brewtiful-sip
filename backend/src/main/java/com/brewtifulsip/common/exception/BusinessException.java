package com.brewtifulsip.common.exception;

import lombok.Getter;

/**
 * 도메인 규칙 위반 등 비즈니스 예외. 전역 핸들러가 ErrorCode 기반으로 응답을 생성한다.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
