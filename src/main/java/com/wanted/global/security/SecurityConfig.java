package com.wanted.global.security;

import com.wanted.global.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers("/member/*").permitAll() // 회원가입, 로그인 인증 없이 접근 가능
                                .requestMatchers(HttpMethod.GET, "/article").permitAll()
                                .requestMatchers(HttpMethod.GET, "/article/*").permitAll() // 게시글 조회 인증 없이 가능
                                .anyRequest().authenticated() // 나머지 인증된 사용자만 가능
                )
                .cors().disable() // 다른 도메인에서 API 호출 가능
                .csrf().disable() // CSRF 토큰 끄기
                .httpBasic().disable() // httpBasic 로그인 끄기
                .formLogin().disable() // 폼 로그인 끄기
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(STATELESS)
                ) // 세션끄기
                .addFilterBefore(
                        jwtAuthorizationFilter, // 엑세스 토큰으로 부터 로그인 처리
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

}
