package com.reservation.dto.member;

import com.reservation.domain.MemberEntity;
import com.reservation.type.MemberType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String memberName;
    private String phoneNumber;
    private MemberType memberType;

    public static MemberDto fromEntity(MemberEntity member) {
        return MemberDto.builder()
                .id(member.getId())
                .memberName(member.getUsername())
                .phoneNumber(member.getPhoneNumber())
                .memberType(member.getMemberType())
                .build();
    }
}
