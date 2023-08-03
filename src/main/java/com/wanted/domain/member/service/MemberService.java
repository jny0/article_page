package com.wanted.domain.member.service;

import com.wanted.domain.member.dto.JoinResponse;
import com.wanted.domain.member.dto.LoginResponse;
import com.wanted.domain.member.dto.MemberRequest;
import com.wanted.domain.member.entity.Member;
import com.wanted.domain.member.repository.MemberRepository;
import com.wanted.global.dto.ResponseDTO;
import com.wanted.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseDTO<JoinResponse> join(MemberRequest memberRequest){
        if(isEmailAlreadyExists(memberRequest.getEmail())){
            return ResponseDTO.of("F-1", "해당 이메일은 이미 사용중입니다.");
        }
        Member member = new Member(memberRequest.getEmail(), passwordEncoder.encode(memberRequest.getPassword()));
        memberRepository.save(member);
        return ResponseDTO.of("S-1", "회원가입 성공", new JoinResponse(member));
    }

    @Transactional
    public ResponseDTO<LoginResponse> login(MemberRequest memberRequest){
        Member member = findByEmail(memberRequest.getEmail()).orElse(null);
        if(member == null){
            return ResponseDTO.of("F-1", "존재하지 않는 회원입니다.");
        }

        if(!passwordEncoder.matches(memberRequest.getPassword(), member.getPassword())){
            return ResponseDTO.of("F-2", "비밀번호를 확인해주세요.");
        }
        String accessToken = generateAccessToken(member);
        return ResponseDTO.of("S-1", "로그인 성공", new LoginResponse(member, accessToken));
    }

    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);
    }

    private boolean isEmailAlreadyExists(String email) {
        Optional<Member> existingMember = findByEmail(email);
        return existingMember.isPresent();
    }

    private String generateAccessToken(Member member){
        return jwtTokenProvider.generateToken(member.toClaims());
    }
}
