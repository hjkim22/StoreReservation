package com.reservation.dto.review;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateDto {

    @NotNull(message = "리뷰 ID는 필수입니다.")
    private Long id;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 200, message = "내용은 200자를 초과할 수 없습니다.")
    private String content;

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1)
    @Max(value = 5)
    private int rating;
}
