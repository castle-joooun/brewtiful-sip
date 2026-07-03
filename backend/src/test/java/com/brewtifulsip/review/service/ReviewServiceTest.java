package com.brewtifulsip.review.service;

import com.brewtifulsip.common.exception.BusinessException;
import com.brewtifulsip.menu.domain.Menu;
import com.brewtifulsip.order.domain.Order;
import com.brewtifulsip.order.domain.OrderItem;
import com.brewtifulsip.order.domain.OrderStatus;
import com.brewtifulsip.order.repository.OrderItemRepository;
import com.brewtifulsip.review.domain.Review;
import com.brewtifulsip.review.dto.ReviewCreateRequest;
import com.brewtifulsip.review.dto.ReviewResponse;
import com.brewtifulsip.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @InjectMocks private ReviewService reviewService;

    private OrderItem itemWithStatus(OrderStatus status) {
        Menu menu = Menu.builder().name("브루잉 커피").price(5500).requiresBean(true).active(true).build();
        Order order = Order.create(); // RECEIVED
        OrderItem item = OrderItem.builder().menu(menu).quantity(1).unitPrice(5500).build();
        order.addItem(item);
        if (status == OrderStatus.READY) {
            order.changeStatus(OrderStatus.READY);
        }
        return item;
    }

    @Test
    void createReview_는_준비완료_전이면_예외() {
        OrderItem item = itemWithStatus(OrderStatus.RECEIVED);
        when(orderItemRepository.findById(10L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> reviewService.createReview(
                10L, item.getOrder().getOrderToken(), new ReviewCreateRequest(5, "좋아요")))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void createReview_는_모든_조건_충족시_저장한다() {
        OrderItem item = itemWithStatus(OrderStatus.READY);
        when(orderItemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(reviewRepository.existsByOrderItem_Id(10L)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(inv -> inv.getArgument(0));

        ReviewResponse response = reviewService.createReview(
                10L, item.getOrder().getOrderToken(), new ReviewCreateRequest(5, "맛있어요"));

        assertThat(response.rating()).isEqualTo(5);
        assertThat(response.menuName()).isEqualTo("브루잉 커피");
    }
}
