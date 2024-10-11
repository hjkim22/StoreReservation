package com.reservation.service;

import com.reservation.domain.MemberEntity;
import com.reservation.dto.member.*;
import com.reservation.exception.ApplicationException;
import com.reservation.repository.MemberRepository;
import com.reservation.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.reservation.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;

    // 순환 종속성 방지로 Setter 주입
    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * 회원가입
     * @param signUpRequest 사용자 가입 정보를 포함하는 요청 객체
     * @return 등록된 회원을 나타내는 MemberDto
     * @throws ApplicationException 사용자 이름이 이미 존재하는 경우
     */
    public SignUpDto.Response signUp(SignUpDto.Request signUpRequest) {
        if (memberRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ApplicationException(ALREADY_EXIST_USER);
        }

        MemberEntity savedMember = createMember(signUpRequest);

        return new SignUpDto.Response(savedMember.getId(), savedMember.getUsername(), "회원가입 성공");
    }

    /**
     * 로그인
     * @param signInRequest 사용자 로그인 정보를 포함하는 요청 객체
     * @return 로그인한 회원을 나타내는 MemberDto
     * @throws ApplicationException 사용자 이름이 존재하지 않거나 비밀번호가 일치하지 않는 경우
     */
    public SignInDto.Response signIn(SignInDto.Request signInRequest) {
        MemberEntity member = memberRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword())) {
            throw new ApplicationException(PASSWORD_NOT_MATCH);
        }

        // 토큰 생성
        String token = tokenProvider.generateToken(member.getUsername(), member.getMemberType());

        // 로그인된 사용자 정보와 JWT 토큰을 반환
        return new SignInDto.Response(token, member.getId(), member.getUsername(), "로그인 성공");
    }

    /**
     * 회원(memberId) 정보 조회
     * @param memberId 조회할 회원의 ID
     * @return 조회된 회원을 나타내는 MemberDto
     * @throws ApplicationException 회원을 찾을 수 없는 경우
     */
    public MemberDto getMemberById(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        return MemberDto.fromEntity(member);
    }

    /**
     * 회원(username) 정보 조회
     * @param username 조회할 사용자 이름
     * @return 조회된 회원 정보를 나타내는 MemberDto
     * @throws ApplicationException 회원을 찾을 수 없는 경우
     */
    public MemberDto getMemberByUsername(String username) {
        MemberEntity member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        return MemberDto.fromEntity(member);
    }

    /**
     * 회원 정보 수정
     * @param memberId      수정할 회원의 ID
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
     * 회원 삭제
     * @param deleteRequest 삭제할 회원의 ID
     * @throws ApplicationException 회원을 찾을 수 없는 경우
     */
    public void deleteMember(MemberDeleteDto deleteRequest) {
        MemberEntity member = memberRepository.findById(deleteRequest.getId())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(deleteRequest.getPassword(), member.getPassword())) {
            throw new ApplicationException(PASSWORD_NOT_MATCH);
        }

        memberRepository.delete(member);
    }

    /**
     * 회원 엔티티 생성
     * @param signUpRequest 회원 가입 요청 객체
     * @return 생성된 회원 엔티티
     */
    private MemberEntity createMember(SignUpDto.Request signUpRequest) {
        return memberRepository.save(
                MemberEntity.builder()
                        .username(signUpRequest.getUsername())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .phoneNumber(signUpRequest.getPhoneNumber())
                        .memberType(signUpRequest.getMemberType())
                        .build()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
    }
}
