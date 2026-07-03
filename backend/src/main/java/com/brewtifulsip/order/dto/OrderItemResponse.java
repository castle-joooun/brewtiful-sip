package com.brewtifulsip.order.dto;

public record OrderItemResponse(
        Long orderItemId,
        String menuName,
        String beanName,
        int quantity,
        int unitPrice,
        boolean reviewed
) {
}
