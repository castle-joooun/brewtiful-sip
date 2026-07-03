package com.brewtifulsip.order.dto;

import com.brewtifulsip.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long orderId,
        OrderStatus status,
        LocalDateTime readyAt,
        List<OrderItemResponse> items
) {
}
