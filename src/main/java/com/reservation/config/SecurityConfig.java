package com.reservation.config;

import com.reservation.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;

    /**
     * 보안 필터 체인 구성
     * @param http HttpSecurity 객체 구성
     * @return 구성된 SecurityFilterChain
     * @throws Exception 구성 중 오류 발생 시 예외 처리
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증을 비활성화
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호를 비활성화
                .sessionManagement(sessionManagement -> // STATELESS -> 서버에서 세션을 생성하거나 사용하지 않음
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 회원가입 / 로그인 API 누구나 접근 허용
                        .requestMatchers("/api/v1/members/sign-up", "/api/v1/members/sign-in").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청 인증 필요
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // SecurityFilterChain 반환
    }

    // 비밀번호 인코더를 생성
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
