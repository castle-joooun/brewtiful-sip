package com.brewtifulsip.order.domain;

import com.brewtifulsip.common.entity.BaseTimeEntity;
import com.brewtifulsip.common.exception.BusinessException;
import com.brewtifulsip.common.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 주문(장바구니 단위). 손님은 orderToken(UUID)으로 조회하고, 운영자는 상태를 전이한다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_token", nullable = false, unique = true, length = 36)
    private String orderToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "ready_at")
    private LocalDateTime readyAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public static Order create() {
        Order order = new Order();
        order.orderToken = UUID.randomUUID().toString();
        order.status = OrderStatus.RECEIVED;
        return order;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.assignOrder(this);
    }

    /** 상태 전이. READY 진입 시 준비완료 시각을 기록한다(리뷰 기간 계산 기준). */
    public void changeStatus(OrderStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATE_TRANSITION);
        }
        this.status = target;
        if (target == OrderStatus.READY) {
            this.readyAt = LocalDateTime.now();
        }
    }
}
