package com.brewtifulsip.menu.repository;

import com.brewtifulsip.common.config.JpaAuditingConfig;
import com.brewtifulsip.menu.domain.Menu;
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
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void findByActiveTrue_는_활성_메뉴만_반환한다() {
        menuRepository.save(Menu.builder().name("아이스 아메리카노").price(4000).requiresBean(false).active(true).build());
        menuRepository.save(Menu.builder().name("단종 메뉴").price(4000).requiresBean(false).active(false).build());

        List<Menu> result = menuRepository.findByActiveTrueOrderByIdAsc();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("아이스 아메리카노");
    }
}
