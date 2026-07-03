package com.brewtifulsip.common.security;

import com.brewtifulsip.common.exception.BusinessException;
import com.brewtifulsip.common.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 운영자 마스터 코드 검증 (ADR-0003). 타이밍 공격 방지를 위해 상수시간 비교를 사용한다.
 */
@Component
public class MasterCodeVerifier {

    private final MasterCodeProperties properties;

    public MasterCodeVerifier(MasterCodeProperties properties) {
        this.properties = properties;
    }

    public void verify(String provided) {
        String expected = properties.masterCode();
        if (expected == null || expected.isBlank()) {
            // 설정 누락 시 보안상 전부 거부한다.
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "마스터 코드가 설정되지 않았습니다.");
        }
        if (provided == null || !MessageDigest.isEqual(
                provided.getBytes(StandardCharsets.UTF_8),
                expected.getBytes(StandardCharsets.UTF_8))) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
