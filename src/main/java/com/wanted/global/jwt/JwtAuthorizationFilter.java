package com.wanted.global.jwt;


import com.wanted.domain.member.entity.Member;
import com.wanted.domain.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization"); // 헤더에서 Authorization 값을 가져오기

        if (bearerToken != null) {
            String token = bearerToken.substring("Bearer ".length());

            // 토큰 유효한지 체크
            if (jwtTokenProvider.verify(token)) {
                Map<String, Object> claims = jwtTokenProvider.getClaims(token);
                long id = (int) claims.get("id");

                Member member = memberService.findById(id).orElseThrow();
                forceAuthentication(member);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 강제로 로그인 처리
    private void forceAuthentication(Member member) {
        User user = new User(member.getEmail(), member.getPassword(), member.getAuthorities());

        // 스프링 시큐리티 객체에 저장할 authentication 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        user,
                        null,
                        member.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext(); // 스프링 시큐리티 내에 authentication 객체를 저장할 context 생성
        context.setAuthentication(authentication); // context에 authentication 객체를 저장
        SecurityContextHolder.setContext(context); // 스프링 시큐리티에 context를 등록
    }
}