package com.brewtifulsip.review.dto;

import com.brewtifulsip.review.domain.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long reviewId,
        Long orderItemId,
        Long menuId,
        String menuName,
        int rating,
        String content,
        LocalDateTime createdAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getOrderItem().getId(),
                review.getOrderItem().getMenu().getId(),
                review.getOrderItem().getMenu().getName(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}
