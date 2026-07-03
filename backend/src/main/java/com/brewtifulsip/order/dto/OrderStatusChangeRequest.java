package com.brewtifulsip.order.dto;

import com.brewtifulsip.order.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusChangeRequest(@NotNull OrderStatus status) {
}
