package com.brewtifulsip.order.dto;

import com.brewtifulsip.order.domain.OrderStatus;

public record OrderCreatedResponse(Long orderId, String orderToken, OrderStatus status) {
}
