package com.reservation.dto.review;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDeleteDto {

    @NotNull(message = "리뷰 ID는 필수입니다.")
    private Long id;
}
