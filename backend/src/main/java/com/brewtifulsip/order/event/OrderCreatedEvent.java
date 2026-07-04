package com.brewtifulsip.order.event;

/**
 * 신규 주문 생성 이벤트. 운영자 대시보드 알림(콘솔/SSE)의 트리거.
 * ApplicationEventPublisher로 발행하며, Phase 3에서 Kafka 이벤트로 확장할 지점이다.
 */
public record OrderCreatedEvent(Long orderId, String orderToken, int itemCount) {
}
