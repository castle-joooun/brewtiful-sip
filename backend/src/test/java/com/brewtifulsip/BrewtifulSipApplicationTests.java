package com.brewtifulsip;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 스캐폴딩 스모크 테스트: test 프로파일(H2, Flyway 비활성)에서 애플리케이션 컨텍스트가
 * 정상적으로 로드되는지 확인한다.
 */
@ActiveProfiles("test")
@SpringBootTest
class BrewtifulSipApplicationTests {

    @Test
    void contextLoads() {
    }
}
