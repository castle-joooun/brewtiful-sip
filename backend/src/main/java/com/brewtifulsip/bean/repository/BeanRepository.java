package com.brewtifulsip.bean.repository;

import com.brewtifulsip.bean.domain.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeanRepository extends JpaRepository<Bean, Long> {

    /** 노출 가능한(active) 원두만 id 오름차순으로 조회한다. */
    List<Bean> findByActiveTrueOrderByIdAsc();
}
