package com.reservation.controller;

import com.reservation.dto.member.MemberDto;
import com.reservation.dto.member.MemberUpdateDto;
import com.reservation.dto.member.SignInDto;
import com.reservation.dto.member.SignUpDto;
import com.reservation.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 API
     * @param signUpRequest 회원가입 요청 데이터를 담은 객체
     * @return 생성된 회원 정보를 담은 MemberDto
     */
    @PostMapping("/sign-up")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto.Request signUpRequest) {
        MemberDto newMember = memberService.signUp(signUpRequest);
        return ResponseEntity.ok(newMember);
    }

    /**
     * 로그인 API
     * @param signInRequest 로그인 요청 데이터를 담은 객체
     * @return 로그인한 회원 정보를 담은 MemberDto
     */
    @PostMapping("/sign-in")
    public ResponseEntity<MemberDto> signIn(@RequestBody SignInDto.Request signInRequest) {
        MemberDto loggedInMember = memberService.signIn(signInRequest);
        return ResponseEntity.ok(loggedInMember);
    }

    /**
     * 특정 회원 조회 API
     * @param memberId 조회할 회원의 ID
     * @return 조회된 회원 정보를 담은 MemberDto
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long memberId) {
        MemberDto member = memberService.getMember(memberId);
        return ResponseEntity.ok(member);
    }

    /**
     * 회원 정보 수정 API
     * @param memberId 수정할 회원의 ID
     * @param updateRequest 수정할 정보를 담은 객체
     * @return 수정된 회원 정보를 담은 MemberDto
     */
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long memberId, @RequestBody MemberUpdateDto updateRequest) {
        MemberDto updatedMember = memberService.updateMember(memberId, updateRequest);
        return ResponseEntity.ok(updatedMember);
    }

    /**
     * 회원 삭제 API
     * @param memberId 삭제할 회원의 ID
     * @return 성공 응답 코드
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
