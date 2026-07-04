package com.brewtifulsip.notification.sse;

import com.brewtifulsip.order.event.OrderCreatedEvent;
import com.brewtifulsip.order.event.OrderStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SSE 구독자 레지스트리 + 도메인 이벤트 브로드캐스터.
 * - 운영자 대시보드: 모든 신규 주문/상태 변경을 수신.
 * - 손님: 자신의 주문(orderId) 상태 변경만 수신.
 * 도메인 이벤트를 구독하므로 주문 서비스와 직접 결합되지 않는다.
 */
@Slf4j
@Service
public class OrderSseService {

    private static final long TIMEOUT_MS = 30 * 60 * 1000L; // 30분

    private final List<SseEmitter> ownerEmitters = new CopyOnWriteArrayList<>();
    private final Map<Long, List<SseEmitter>> customerEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribeOwner() {
        SseEmitter emitter = new SseEmitter(TIMEOUT_MS);
        ownerEmitters.add(emitter);
        emitter.onCompletion(() -> ownerEmitters.remove(emitter));
        emitter.onTimeout(() -> ownerEmitters.remove(emitter));
        send(emitter, "connected", "owner");
        return emitter;
    }

    public SseEmitter subscribeCustomer(Long orderId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT_MS);
        customerEmitters.computeIfAbsent(orderId, key -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> removeCustomer(orderId, emitter));
        emitter.onTimeout(() -> removeCustomer(orderId, emitter));
        send(emitter, "connected", orderId);
        return emitter;
    }

    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        ownerEmitters.forEach(emitter -> send(emitter, "order-created", event));
    }

    @EventListener
    public void onOrderStatusChanged(OrderStatusChangedEvent event) {
        ownerEmitters.forEach(emitter -> send(emitter, "order-status", event));
        List<SseEmitter> emitters = customerEmitters.get(event.orderId());
        if (emitters != null) {
            emitters.forEach(emitter -> send(emitter, "order-status", event));
        }
    }

    private void removeCustomer(Long orderId, SseEmitter emitter) {
        List<SseEmitter> emitters = customerEmitters.get(orderId);
        if (emitters != null) {
            emitters.remove(emitter);
        }
    }

    private void send(SseEmitter emitter, String eventName, Object data) {
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException | IllegalStateException e) {
            emitter.completeWithError(e);
        }
    }
}
