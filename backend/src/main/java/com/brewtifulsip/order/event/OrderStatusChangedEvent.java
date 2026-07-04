package com.brewtifulsip.order.event;

import com.brewtifulsip.order.domain.OrderStatus;

/**
 * 주문 상태 변경 이벤트. 손님 주문 화면/운영자 대시보드의 실시간 반영 트리거.
 */
public record OrderStatusChangedEvent(Long orderId, String orderToken, OrderStatus status) {
}
