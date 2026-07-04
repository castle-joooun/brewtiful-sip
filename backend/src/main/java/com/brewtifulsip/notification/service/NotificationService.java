package com.brewtifulsip.notification.service;

import com.brewtifulsip.order.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Phase 1 알림: 신규 주문 이벤트를 구독해 콘솔 로그로 운영자에게 알린다.
 * 이벤트 기반이라 주문 도메인과 결합되지 않는다(옵저버 패턴).
 * Phase 2에서 카카오톡/슬랙, Phase 3에서 Kafka 컨슈머로 확장한다.
 */
@Slf4j
@Service
public class NotificationService {

    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("[신규 주문] orderId={}, token={}, itemCount={}",
                event.orderId(), event.orderToken(), event.itemCount());
    }
}
