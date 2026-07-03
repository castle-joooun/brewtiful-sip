package com.brewtifulsip.review.api;

import com.brewtifulsip.review.dto.ReviewCreateRequest;
import com.brewtifulsip.review.dto.ReviewResponse;
import com.brewtifulsip.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/order-items/{orderItemId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create(@PathVariable Long orderItemId,
                                 @RequestHeader("X-Order-Token") String orderToken,
                                 @Valid @RequestBody ReviewCreateRequest request) {
        return reviewService.createReview(orderItemId, orderToken, request);
    }

    @GetMapping("/reviews")
    public List<ReviewResponse> list(@RequestParam(required = false) Long menuId) {
        return reviewService.listReviews(menuId);
    }
}
