package com.brewtifulsip.common.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 운영자 전용 요청에 대해 X-Master-Code 헤더를 검증한다 (ADR-0003).
 * 적용 경로는 {@link com.brewtifulsip.common.config.WebConfig} 에서 등록한다.
 */
@Component
public class OwnerAuthInterceptor implements HandlerInterceptor {

    public static final String MASTER_CODE_HEADER = "X-Master-Code";

    private final MasterCodeVerifier verifier;

    public OwnerAuthInterceptor(MasterCodeVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        verifier.verify(request.getHeader(MASTER_CODE_HEADER));
        return true;
    }
}
