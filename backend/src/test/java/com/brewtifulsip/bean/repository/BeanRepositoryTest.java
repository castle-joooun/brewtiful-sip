package com.brewtifulsip.bean.repository;

import com.brewtifulsip.bean.domain.Bean;
import com.brewtifulsip.common.config.JpaAuditingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
@DataJpaTest
class BeanRepositoryTest {

    @Autowired
    private BeanRepository beanRepository;

    @Test
    void findByActiveTrue_는_활성_원두만_반환하고_생성시각을_채운다() {
        beanRepository.save(Bean.builder().name("활성원두").active(true).build());
        beanRepository.save(Bean.builder().name("품절원두").active(false).build());

        List<Bean> result = beanRepository.findByActiveTrueOrderByIdAsc();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("활성원두");
        assertThat(result.get(0).getCreatedAt()).isNotNull();
    }
}
