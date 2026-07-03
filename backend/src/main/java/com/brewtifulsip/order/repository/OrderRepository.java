package com.brewtifulsip.order.repository;

import com.brewtifulsip.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
