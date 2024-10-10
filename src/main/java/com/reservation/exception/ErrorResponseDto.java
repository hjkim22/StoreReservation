package com.reservation.exception;

import com.reservation.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private int statusCode;
    private ErrorCode errorCode;
    private String errorMessage;
}
