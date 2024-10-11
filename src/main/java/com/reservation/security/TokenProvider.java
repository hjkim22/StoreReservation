package com.reservation.security;

import com.reservation.exception.ApplicationException;
import com.reservation.service.MemberService;
import com.reservation.type.ErrorCode;
import com.reservation.type.MemberType;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    private static final String KEY_ROLES = "roles";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간

    private final MemberService memberService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    /**
     * 주어진 사용자 이름과 역할 목록으로 JWT 토큰을 생성
     *
     * @param username   사용자 이름
     * @param memberType 사용자 역할 목록
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String username, MemberType memberType) {

        return Jwts.builder()
                .setSubject(username)
                .claim(KEY_ROLES, memberType.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 인증 정보를 가져옴
     *
     * @param jwt JWT 토큰
     * @return 인증 정보
     */
    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    /**
     * JWT 토큰에서 사용자 이름 추출
     *
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    /**
     * JWT 토큰의 유효성 검사
     *
     * @param token JWT 토큰
     * @return 유효성 검사 결과 (true: 유효, false: 유효하지 않음)
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    /**
     * JWT 토큰에서 클레임 파싱
     *
     * @param token JWT 토큰
     * @return 클레임
     * @throws ExpiredJwtException 만료된 토큰일 경우 발생
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(ErrorCode.TOKEN_TIME_OUT);
        } catch (JwtException e) {
            throw new ApplicationException(ErrorCode.WRONG_TOKEN);
        }
    }
}
