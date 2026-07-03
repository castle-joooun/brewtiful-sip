package com.brewtifulsip.bean.service;

import com.brewtifulsip.bean.domain.Bean;
import com.brewtifulsip.bean.dto.BeanResponse;
import com.brewtifulsip.bean.repository.BeanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeanServiceTest {

    @Mock
    private BeanRepository beanRepository;

    @InjectMocks
    private BeanService beanService;

    @Test
    void getActiveBeans_는_활성_원두를_DTO로_변환한다() {
        when(beanRepository.findByActiveTrueOrderByIdAsc())
                .thenReturn(List.of(
                        Bean.builder().name("예가체프").origin("에티오피아").active(true).build()));

        List<BeanResponse> result = beanService.getActiveBeans();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("예가체프");
        assertThat(result.get(0).origin()).isEqualTo("에티오피아");
    }
}
