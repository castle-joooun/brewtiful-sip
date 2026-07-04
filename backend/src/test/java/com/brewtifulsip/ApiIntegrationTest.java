package com.brewtifulsip;

import com.brewtifulsip.bean.domain.Bean;
import com.brewtifulsip.bean.repository.BeanRepository;
import com.brewtifulsip.menu.domain.Menu;
import com.brewtifulsip.menu.repository.MenuRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 웹 계층 통합 테스트: 컨트롤러 + 전역 예외 처리 + 운영자 인증 인터셉터를 실제 요청 흐름으로 검증한다.
 * test 프로파일(H2)에서 동작하며, @Transactional로 각 테스트 후 롤백한다.
 */
@ActiveProfiles("test")
// 마스터 코드를 테스트 속성으로 고정한다. (OS 환경변수 BREWTIFUL_MASTER_CODE가 설정돼 있어도
//  테스트 속성이 우선하므로, 주변 셸 환경에 흔들리지 않는다.)
@SpringBootTest(properties = "brewtiful.master-code=test-master-code")
@AutoConfigureMockMvc
@Transactional
class ApiIntegrationTest {

    private static final String MASTER = "test-master-code";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BeanRepository beanRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getBeans_는_활성_원두만_반환한다() throws Exception {
        beanRepository.save(Bean.builder().name("활성원두").active(true).build());
        beanRepository.save(Bean.builder().name("품절원두").active(false).build());

        mockMvc.perform(get("/beans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void 주문_생성_후_운영자_상태변경은_마스터코드를_요구한다() throws Exception {
        Menu menu = menuRepository.save(
                Menu.builder().name("아이스 아메리카노").price(4000).requiresBean(false).active(true).build());

        String body = "{\"items\":[{\"menuId\":" + menu.getId() + ",\"quantity\":1}]}";
        MvcResult created = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("RECEIVED"))
                .andReturn();

        JsonNode node = objectMapper.readTree(created.getResponse().getContentAsString());
        long orderId = node.get("orderId").asLong();

        // 마스터코드 없으면 인터셉터가 401
        mockMvc.perform(patch("/orders/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"READY\"}"))
                .andExpect(status().isUnauthorized());

        // 마스터코드가 있으면 준비완료 처리
        mockMvc.perform(patch("/orders/" + orderId + "/status")
                        .header("X-Master-Code", MASTER)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"READY\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READY"));
    }

    @Test
    void 미완료_주문목록은_마스터코드가_있어야_조회된다() throws Exception {
        mockMvc.perform(get("/orders/pending"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/orders/pending").header("X-Master-Code", MASTER))
                .andExpect(status().isOk());
    }
}
