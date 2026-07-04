package com.brewtifulsip.common.security;

import com.brewtifulsip.common.dto.ErrorResponse;
import com.brewtifulsip.common.exception.BusinessException;
import com.brewtifulsip.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * 운영자 전용 요청에 대해 X-Master-Code 헤더를 검증한다 (ADR-0003).
 * 적용 경로는 {@link com.brewtifulsip.common.config.WebConfig} 에서 등록한다.
 *
 * 인증 실패 시 예외를 던져 @ControllerAdvice에 위임하지 않고 직접 401 응답을 작성한다.
 * (인터셉터 preHandle에서 던진 예외는 컨트롤러 예외와 처리 경로가 달라 응답 상태가 일관되지 않기
 *  때문. 인터셉터 기반 인증의 표준 패턴대로 여기서 응답을 확정한다.)
 */
@Component
public class OwnerAuthInterceptor implements HandlerInterceptor {

    public static final String MASTER_CODE_HEADER = "X-Master-Code";

    private final MasterCodeVerifier verifier;
    private final ObjectMapper objectMapper;

    public OwnerAuthInterceptor(MasterCodeVerifier verifier, ObjectMapper objectMapper) {
        this.verifier = verifier;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            verifier.verify(request.getHeader(MASTER_CODE_HEADER));
            return true;
        } catch (BusinessException e) {
            writeError(response, e.getErrorCode(), e.getMessage());
            return false;
        }
    }

    private void writeError(HttpServletResponse response, ErrorCode code, String message) throws Exception {
        response.setStatus(code.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(code, message)));
    }
}
