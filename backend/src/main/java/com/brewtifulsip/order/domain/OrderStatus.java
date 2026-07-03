package com.brewtifulsip.order.domain;

/**
 * 주문 상태. 전이 규칙: RECEIVED → (PREPARING) → READY. READY는 종료 상태.
 */
public enum OrderStatus {
    RECEIVED,
    PREPARING,
    READY;

    public boolean canTransitionTo(OrderStatus target) {
        return switch (this) {
            case RECEIVED -> target == PREPARING || target == READY;
            case PREPARING -> target == READY;
            case READY -> false;
        };
    }
}
