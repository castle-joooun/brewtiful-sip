package com.brewtifulsip.common.config;

import com.brewtifulsip.common.security.OwnerAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 운영자 인증 인터셉터 등록. 운영자 전용 엔드포인트에만 마스터 코드 검증을 적용한다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final OwnerAuthInterceptor ownerAuthInterceptor;

    public WebConfig(OwnerAuthInterceptor ownerAuthInterceptor) {
        this.ownerAuthInterceptor = ownerAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 운영자 전용 경로: 상태 변경, 대시보드 SSE, 미완료 주문 목록
        registry.addInterceptor(ownerAuthInterceptor)
                .addPathPatterns("/orders/*/status", "/orders/stream", "/orders/pending");
    }
}
