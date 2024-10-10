package com.reservation.dto.member;

import com.reservation.type.MemberType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SignUpDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "사용자 이름은 필수입니다.")
        @Size(min = 3, max = 50, message = "사용자 이름은 3자 이상, 50자 이하로 입력해야 합니다.")
        private String memberName;

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
        private String password;

        @NotBlank(message = "전화번호는 필수입니다.")
        private String phoneNumber;

        private MemberType memberType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String memberName;
        private String message;
    }
}
