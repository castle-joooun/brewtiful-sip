package com.brewtifulsip.bean.api;

import com.brewtifulsip.bean.dto.BeanResponse;
import com.brewtifulsip.bean.service.BeanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 원두 조회 API. Controller는 얇게 유지하고 로직은 Service에 위임한다(CLAUDE.md 4장).
 */
@RestController
@RequestMapping("/beans")
public class BeanController {

    private final BeanService beanService;

    public BeanController(BeanService beanService) {
        this.beanService = beanService;
    }

    @GetMapping
    public List<BeanResponse> listBeans() {
        return beanService.getActiveBeans();
    }
}
