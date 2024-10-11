package com.reservation.security;

import com.reservation.exception.ApplicationException;
import com.reservation.type.ErrorCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final ApplicationContext applicationContext;
    private TokenProvider tokenProvider;

    /**
     * 요청을 필터링하여 JWT 인증을 수행
     *
     * @param request     HttpServletRequest 객체
     * @param response    HttpServletResponse 객체
     * @param filterChain 필터 체인
     * @throws ServletException Servlet 처리 중 발생할 수 있는 예외
     * @throws IOException      I/O 처리 중 발생할 수 있는 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (tokenProvider == null) {
            tokenProvider = applicationContext.getBean(TokenProvider.class);
        }

        String token = this.resolveTokenFromRequest(request); // 요청에서 JWT 토큰을 추출

        if (!StringUtils.hasText(token)) {
            throw new ApplicationException(ErrorCode.LOGIN_REQUIRED);
        }

        if (!this.tokenProvider.validateToken(token)) {
            throw new ApplicationException(ErrorCode.WRONG_TOKEN);
        }

        // JWT 에서 인증 정보를 가져옴
        Authentication auth;
        try {
            auth = this.tokenProvider.getAuthentication(token);
        } catch (JwtException e) {
            throw new ApplicationException(ErrorCode.WRONG_TYPE_SIGNATURE);
        }

        // SecurityContext 에 인증 정보를 설정
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 다음 필터로 요청/응답 전달
        filterChain.doFilter(request, response);
    }

    /**
     * 요청에서 JWT 토큰 추출
     *
     * @param request HttpServletRequest 객체
     * @return 추출한 JWT 토큰, 없거나 유효하지 않으면 null
     */
    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        // 토큰이 null 이 아니고 지정한 접두사로 시작하는지 확인
        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            // 접두사 제거 후 토큰만 반환
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
