package com.brewtifulsip.common.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 운영자 마스터 코드 설정 (ADR-0003). 값은 환경변수(BREWTIFUL_MASTER_CODE)로 외부화한다.
 */
@ConfigurationProperties(prefix = "brewtiful")
public record MasterCodeProperties(String masterCode) {
}
