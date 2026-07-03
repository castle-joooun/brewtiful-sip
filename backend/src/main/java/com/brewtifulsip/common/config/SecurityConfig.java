package com.brewtifulsip.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Phase 1 보안 설정.
 * 손님 API는 공개이며, 운영자 인증은 Spring Security 사용자 모델이 아니라
 * 마스터 코드 헤더(ADR-0003, {@code OwnerAuthInterceptor})로 별도 처리한다.
 * 따라서 기본 폼로그인/베이직 인증을 끄고 모든 요청을 통과시킨다.
 * 정식 인증/인가는 Phase 2에서 승격한다.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
