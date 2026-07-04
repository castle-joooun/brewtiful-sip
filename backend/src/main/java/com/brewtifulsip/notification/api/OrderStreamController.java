package com.brewtifulsip.notification.api;

import com.brewtifulsip.notification.sse.OrderSseService;
import com.brewtifulsip.order.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE 스트림 엔드포인트.
 * - 운영자: GET /orders/stream (OwnerAuthInterceptor의 X-Master-Code로 보호)
 * - 손님:   GET /orders/{orderId}/stream?token=... (주문 토큰 검증 후 구독)
 */
@RestController
public class OrderStreamController {

    private final OrderSseService sseService;
    private final OrderService orderService;

    public OrderStreamController(OrderSseService sseService, OrderService orderService) {
        this.sseService = sseService;
        this.orderService = orderService;
    }

    @GetMapping("/orders/stream")
    public SseEmitter ownerStream() {
        return sseService.subscribeOwner();
    }

    @GetMapping("/orders/{orderId}/stream")
    public SseEmitter customerStream(@PathVariable Long orderId, @RequestParam String token) {
        orderService.verifyCustomerToken(orderId, token);
        return sseService.subscribeCustomer(orderId);
    }
}
