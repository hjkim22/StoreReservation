package com.reservation.exception;

import com.reservation.type.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.reservation.type.ErrorCode.*;

@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외 처리
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 커스텀 애플리케이션 예외 처리
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseDto> handleApplicationException(ApplicationException e) {
        logError(e);
        return buildErrorResponse(e.getErrorCode());
    }

    // 데이터 무결성 위반 예외 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        logger.error("데이터 무결성 위반 오류가 발생했습니다.", e);
        return buildErrorResponse(INVALID_REQUEST);
    }

    // 사용자 이름을 찾을 수 없는 예외 처리
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFoundException(UsernameNotFoundException e) {
        logger.error("사용자를 찾을 수 없는 오류가 발생했습니다.", e);
        return buildErrorResponse(USER_NOT_FOUND);
    }

    // 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e) {
        logger.error("예외가 발생했습니다.", e);
        return buildErrorResponse(INTERNAL_SERVER_ERROR);
    }

    // 에러 응답 생성 메소드
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(ErrorCode errorCode) {
        ErrorResponseDto response = ErrorResponseDto.builder()
                .statusCode(errorCode.getStatusCode())
                .errorCode(errorCode)
                .errorMessage(errorCode.getDescription())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    // 에러 로깅 메소드
    private void logError(Exception e) {
        logger.error("{} 오류가 발생했습니다.", e.getClass().getSimpleName(), e);
    }
}
