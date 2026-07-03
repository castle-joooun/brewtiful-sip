package com.brewtifulsip.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 활성화. 별도 설정 클래스로 분리해 슬라이스 테스트에서 선택적으로 import 한다.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
