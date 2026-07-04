package com.brewtifulsip.order.repository;

import com.brewtifulsip.order.domain.Order;
import com.brewtifulsip.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /** 운영자 대시보드용: 지정한 상태(예: 미완료)의 주문을 최신순으로 조회한다. */
    List<Order> findByStatusInOrderByIdDesc(Collection<OrderStatus> statuses);
}
