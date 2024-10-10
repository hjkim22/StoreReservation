package com.reservation.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SignInDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "사용자 이름은 필수입니다.")
        private String memberName;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String token; // JWT 토큰
        private Long userId;
        private String memberName;
        private String message;
    }
}