package com.brewtifulsip.order.service;

import com.brewtifulsip.bean.repository.BeanRepository;
import com.brewtifulsip.common.exception.BusinessException;
import com.brewtifulsip.menu.domain.Menu;
import com.brewtifulsip.menu.repository.MenuRepository;
import com.brewtifulsip.order.domain.Order;
import com.brewtifulsip.order.domain.OrderStatus;
import com.brewtifulsip.order.dto.OrderCreateRequest;
import com.brewtifulsip.order.dto.OrderCreatedResponse;
import com.brewtifulsip.order.dto.OrderItemCreateRequest;
import com.brewtifulsip.order.event.OrderCreatedEvent;
import com.brewtifulsip.order.repository.OrderRepository;
import com.brewtifulsip.review.repository.ReviewRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private MenuRepository menuRepository;
    @Mock private BeanRepository beanRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks private OrderService orderService;

    @Test
    void createOrder_는_원두불필요_메뉴를_주문하고_알림을_보낸다() {
        Menu menu = Menu.builder().name("아이스 아메리카노").price(4000).requiresBean(false).active(true).build();
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderCreatedResponse response = orderService.createOrder(
                new OrderCreateRequest(List.of(new OrderItemCreateRequest(1L, null, 2))));

        assertThat(response.status()).isEqualTo(OrderStatus.RECEIVED);
        assertThat(response.orderToken()).isNotBlank();
        verify(eventPublisher).publishEvent(any(OrderCreatedEvent.class));
    }

    @Test
    void createOrder_는_원두필요_메뉴에_원두가_없으면_예외() {
        Menu menu = Menu.builder().name("브루잉 커피").price(5500).requiresBean(true).active(true).build();
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        assertThatThrownBy(() -> orderService.createOrder(
                new OrderCreateRequest(List.of(new OrderItemCreateRequest(1L, null, 1)))))
                .isInstanceOf(BusinessException.class);
    }
}
