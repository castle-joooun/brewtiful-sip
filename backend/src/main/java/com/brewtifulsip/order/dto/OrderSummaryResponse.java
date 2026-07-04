package com.brewtifulsip.order.dto;

import com.brewtifulsip.order.domain.OrderStatus;

import java.time.LocalDateTime;

/**
 * 운영자 대시보드용 주문 요약. 미완료 주문 목록 조회에 사용한다.
 */
public record OrderSummaryResponse(
        Long orderId,
        OrderStatus status,
        int itemCount,
        LocalDateTime createdAt
) {
}
