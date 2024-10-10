package com.reservation.dto.review;

import com.reservation.domain.ReviewEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long memberId;
    private Long storeId;
    private String content;
    private String username;
    private String storeName;
    private int rating;

    public static ReviewDto fromEntity(ReviewEntity review) {
        return ReviewDto.builder()
                .memberId(review.getMember().getId())
                .storeId(review.getStore().getId())
                .content(review.getContent())
                .rating(review.getRating())
                .username(review.getMember().getUsername())
                .storeName(review.getStore().getStoreName())
                .build();
    }
}
