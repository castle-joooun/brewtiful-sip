package com.brewtifulsip.notification.service;

import com.brewtifulsip.order.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Phase 1 알림: 콘솔 로그로 신규 주문을 운영자에게 알린다.
 * Phase 2에서 카카오톡/슬랙, Phase 3에서 Kafka 이벤트로 대체/확장한다.
 */
@Slf4j
@Service
public class NotificationService {

    public void notifyNewOrder(Order order) {
        log.info("[신규 주문] orderId={}, token={}, itemCount={}",
                order.getId(), order.getOrderToken(), order.getItems().size());
    }
}
