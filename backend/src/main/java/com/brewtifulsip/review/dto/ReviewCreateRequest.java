package com.brewtifulsip.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ReviewCreateRequest(
        @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
        @Max(value = 5, message = "평점은 5 이하여야 합니다.")
        int rating,

        @Size(max = 500, message = "후기는 500자 이하여야 합니다.")
        String content
) {
}
