package com.brewtifulsip.menu.service;

import com.brewtifulsip.menu.domain.Menu;
import com.brewtifulsip.menu.dto.MenuResponse;
import com.brewtifulsip.menu.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void getActiveMenus_는_활성_메뉴를_DTO로_변환한다() {
        when(menuRepository.findByActiveTrueOrderByIdAsc())
                .thenReturn(List.of(
                        Menu.builder().name("브루잉 커피").price(5500).requiresBean(true).active(true).build()));

        List<MenuResponse> result = menuService.getActiveMenus();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("브루잉 커피");
        assertThat(result.get(0).requiresBean()).isTrue();
    }
}
