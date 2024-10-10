package com.reservation.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {

    private Long id;
    private String memberName;
    private String phoneNumber;
}
