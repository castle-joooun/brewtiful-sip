package com.brewtifulsip.review.service;

import com.brewtifulsip.common.exception.BusinessException;
import com.brewtifulsip.common.exception.ErrorCode;
import com.brewtifulsip.order.domain.Order;
import com.brewtifulsip.order.domain.OrderItem;
import com.brewtifulsip.order.domain.OrderStatus;
import com.brewtifulsip.order.repository.OrderItemRepository;
import com.brewtifulsip.review.domain.Review;
import com.brewtifulsip.review.dto.ReviewCreateRequest;
import com.brewtifulsip.review.dto.ReviewResponse;
import com.brewtifulsip.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    /** 리뷰 작성 가능 기간: 준비완료(READY) 후 3일 (기능명세서 3.6). */
    private static final long REVIEW_WINDOW_DAYS = 3;

    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;

    public ReviewService(ReviewRepository reviewRepository, OrderItemRepository orderItemRepository) {
        this.reviewRepository = reviewRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public ReviewResponse createReview(Long orderItemId, String orderToken, ReviewCreateRequest request) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "주문 항목을 찾을 수 없습니다."));
        Order order = orderItem.getOrder();

        if (!order.getOrderToken().equals(orderToken)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (order.getStatus() != OrderStatus.READY) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED, "준비완료 후에만 리뷰를 작성할 수 있습니다.");
        }
        if (order.getReadyAt() == null
                || order.getReadyAt().plusDays(REVIEW_WINDOW_DAYS).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED, "리뷰 작성 기간(준비완료 후 3일)이 지났습니다.");
        }
        if (reviewRepository.existsByOrderItem_Id(orderItemId)) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED, "이미 리뷰가 작성된 항목입니다.");
        }

        Review review = Review.builder()
                .orderItem(orderItem)
                .rating(request.rating())
                .content(request.content())
                .build();
        return ReviewResponse.from(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> listReviews(Long menuId) {
        List<Review> reviews = (menuId == null)
                ? reviewRepository.findAllByOrderByIdDesc()
                : reviewRepository.findByOrderItem_Menu_IdOrderByIdDesc(menuId);
        return reviews.stream().map(ReviewResponse::from).toList();
    }
}
