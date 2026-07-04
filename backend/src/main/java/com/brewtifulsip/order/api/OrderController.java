package com.brewtifulsip.order.api;

import com.brewtifulsip.order.dto.OrderCreateRequest;
import com.brewtifulsip.order.dto.OrderCreatedResponse;
import com.brewtifulsip.order.dto.OrderDetailResponse;
import com.brewtifulsip.order.dto.OrderStatusChangeRequest;
import com.brewtifulsip.order.dto.OrderSummaryResponse;
import com.brewtifulsip.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderCreatedResponse create(@Valid @RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    // 운영자 전용: 미완료 주문 목록 (OwnerAuthInterceptor의 X-Master-Code로 보호)
    @GetMapping("/pending")
    public List<OrderSummaryResponse> pending() {
        return orderService.getPendingOrders();
    }

    @GetMapping("/{orderId}")
    public OrderDetailResponse get(@PathVariable Long orderId, @RequestParam String token) {
        return orderService.getOrder(orderId, token);
    }

    // 운영자 전용: OwnerAuthInterceptor(X-Master-Code)로 보호됨.
    @PatchMapping("/{orderId}/status")
    public OrderDetailResponse changeStatus(@PathVariable Long orderId,
                                            @Valid @RequestBody OrderStatusChangeRequest request) {
        return orderService.changeStatus(orderId, request.status());
    }
}
