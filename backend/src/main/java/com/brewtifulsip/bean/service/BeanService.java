package com.brewtifulsip.bean.service;

import com.brewtifulsip.bean.dto.BeanResponse;
import com.brewtifulsip.bean.repository.BeanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 원두 조회 서비스. 트랜잭션 경계를 Service에서 명시(CLAUDE.md 4장), 조회는 readOnly.
 */
@Service
@Transactional(readOnly = true)
public class BeanService {

    private final BeanRepository beanRepository;

    public BeanService(BeanRepository beanRepository) {
        this.beanRepository = beanRepository;
    }

    public List<BeanResponse> getActiveBeans() {
        return beanRepository.findByActiveTrueOrderByIdAsc().stream()
                .map(BeanResponse::from)
                .toList();
    }
}
