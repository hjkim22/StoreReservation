package com.reservation.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDeleteDto {

    private Long id;
    private String password;
}
