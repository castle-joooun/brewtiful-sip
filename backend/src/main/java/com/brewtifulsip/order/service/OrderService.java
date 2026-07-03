package com.brewtifulsip.order.service;

import com.brewtifulsip.bean.domain.Bean;
import com.brewtifulsip.bean.repository.BeanRepository;
import com.brewtifulsip.common.exception.BusinessException;
import com.brewtifulsip.common.exception.ErrorCode;
import com.brewtifulsip.menu.domain.Menu;
import com.brewtifulsip.menu.repository.MenuRepository;
import com.brewtifulsip.notification.service.NotificationService;
import com.brewtifulsip.order.domain.Order;
import com.brewtifulsip.order.domain.OrderItem;
import com.brewtifulsip.order.domain.OrderStatus;
import com.brewtifulsip.order.dto.OrderCreateRequest;
import com.brewtifulsip.order.dto.OrderCreatedResponse;
import com.brewtifulsip.order.dto.OrderDetailResponse;
import com.brewtifulsip.order.dto.OrderItemCreateRequest;
import com.brewtifulsip.order.dto.OrderItemResponse;
import com.brewtifulsip.order.repository.OrderRepository;
import com.brewtifulsip.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final BeanRepository beanRepository;
    private final ReviewRepository reviewRepository;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository,
                        MenuRepository menuRepository,
                        BeanRepository beanRepository,
                        ReviewRepository reviewRepository,
                        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.beanRepository = beanRepository;
        this.reviewRepository = reviewRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public OrderCreatedResponse createOrder(OrderCreateRequest request) {
        Order order = Order.create();
        for (OrderItemCreateRequest itemReq : request.items()) {
            Menu menu = menuRepository.findById(itemReq.menuId())
                    .filter(Menu::isActive)
                    .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "메뉴를 찾을 수 없습니다."));
            Bean bean = resolveBean(menu, itemReq.beanId());
            order.addItem(OrderItem.builder()
                    .menu(menu)
                    .bean(bean)
                    .quantity(itemReq.quantity())
                    .unitPrice(menu.getPrice())
                    .build());
        }
        Order saved = orderRepository.save(order);
        notificationService.notifyNewOrder(saved);
        return new OrderCreatedResponse(saved.getId(), saved.getOrderToken(), saved.getStatus());
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrder(Long orderId, String token) {
        Order order = findOrder(orderId);
        if (!order.getOrderToken().equals(token)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return toDetail(order);
    }

    @Transactional
    public OrderDetailResponse changeStatus(Long orderId, OrderStatus target) {
        Order order = findOrder(orderId);
        order.changeStatus(target);
        return toDetail(order);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "주문을 찾을 수 없습니다."));
    }

    private Bean resolveBean(Menu menu, Long beanId) {
        if (menu.isRequiresBean()) {
            if (beanId == null) {
                throw new BusinessException(ErrorCode.INVALID_BEAN_SELECTION, "원두 선택이 필요한 메뉴입니다.");
            }
            return beanRepository.findById(beanId)
                    .filter(Bean::isActive)
                    .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "원두를 찾을 수 없습니다."));
        }
        if (beanId != null) {
            throw new BusinessException(ErrorCode.INVALID_BEAN_SELECTION, "원두를 선택할 수 없는 메뉴입니다.");
        }
        return null;
    }

    private OrderDetailResponse toDetail(Order order) {
        List<Long> itemIds = order.getItems().stream().map(OrderItem::getId).toList();
        Set<Long> reviewedIds = reviewRepository.findByOrderItem_IdIn(itemIds).stream()
                .map(review -> review.getOrderItem().getId())
                .collect(Collectors.toSet());

        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getMenu().getName(),
                        item.getBean() != null ? item.getBean().getName() : null,
                        item.getQuantity(),
                        item.getUnitPrice(),
                        reviewedIds.contains(item.getId())))
                .toList();

        return new OrderDetailResponse(order.getId(), order.getStatus(), order.getReadyAt(), items);
    }
}
