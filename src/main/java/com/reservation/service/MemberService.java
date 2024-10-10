package com.reservation.service;

import com.reservation.domain.MemberEntity;
import com.reservation.dto.member.MemberDto;
import com.reservation.dto.member.MemberUpdateDto;
import com.reservation.dto.member.SignInDto;
import com.reservation.dto.member.SignUpDto;
import com.reservation.exception.ApplicationException;
import com.reservation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.reservation.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param signUpRequest 사용자 가입 정보를 포함하는 요청 객체
     * @return 등록된 회원을 나타내는 MemberDto
     * @throws ApplicationException 사용자 이름이 이미 존재하는 경우
     */
    public MemberDto signUp(SignUpDto.Request signUpRequest) {
        if (memberRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ApplicationException(ALREADY_EXIST_USER);
        }

        return MemberDto.fromEntity(
                memberRepository.save(
                        MemberEntity.builder()
                                .username(signUpRequest.getUsername())
                                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                                .phoneNumber(signUpRequest.getPhoneNumber())
                                .memberType(signUpRequest.getMemberType())
                                .build()
                )
        );
    }

    /**
     * 로그인
     * @param signInRequest 사용자 로그인 정보를 포함하는 요청 객체
     * @return 로그인한 회원을 나타내는 MemberDto
     * @throws ApplicationException 사용자 이름이 존재하지 않거나 비밀번호가 일치하지 않는 경우
     */
    public MemberDto signIn(SignInDto.Request signInRequest) {
        MemberEntity member = memberRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword())) {
            throw new ApplicationException(PASSWORD_NOT_MATCH);
        }

        return MemberDto.fromEntity(member);
    }

    /**
     * 회원 정보 조회
     * @param memberId 조회할 회원의 ID
     * @return 조회된 회원을 나타내는 MemberDto
     * @throws ApplicationException 회원을 찾을 수 없는 경우
     */
    public MemberDto getMember(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        return MemberDto.fromEntity(member);
    }

    /**
     * 회원 정보 수정
     * @param memberId 수정할 회원의 ID
     * @param updateRequest 수정할 회원 정보를 포함하는 요청 객체
     * @return 수정된 회원을 나타내는 MemberDto
     * @throws ApplicationException 회원을 찾을 수 없는 경우
     */
    public MemberDto updateMember(Long memberId, MemberUpdateDto updateRequest) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        member.setUsername(updateRequest.getUsername());
        member.setPhoneNumber(updateRequest.getPhoneNumber());

        return MemberDto.fromEntity(memberRepository.save(member));
    }

    /**
     * 회원 삭제(탈퇴)
     * @param memberId 삭제할 회원의 ID
     * @throws ApplicationException 회원을 찾을 수 없는 경우
     */
    public void deleteMember(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        memberRepository.delete(member);
    }

    /*
      TODO
      아이디 중복 확인 메서드
      비밀번호 일치 확인 메서드
      회원 이름으로 조회 메서드
      ID로 조회 메서드
      회원 엔티티 생성 메서드
     */
}
