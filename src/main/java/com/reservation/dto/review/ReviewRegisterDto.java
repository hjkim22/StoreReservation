package com.reservation.dto.review;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRegisterDto {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    @NotNull(message = "매장 ID는 필수입니다.")
    private Long storeId;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 200, message = "내용은 200자를 초과할 수 없습니다.")
    private String content;

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1)
    @Max(value = 5)
    private int rating;
}
