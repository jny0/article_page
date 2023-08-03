package com.wanted.domain.member.service;

import com.wanted.domain.member.dto.JoinRequest;
import com.wanted.domain.member.dto.MemberResponse;
import com.wanted.domain.member.entity.Member;
import com.wanted.domain.member.repository.MemberRepository;
import com.wanted.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseDTO<MemberResponse> join(JoinRequest joinRequest){
        Member member = new Member(joinRequest.getEmail(), passwordEncoder.encode(joinRequest.getPassword()));
        memberRepository.save(member);
        return ResponseDTO.of("S-1", "회원가입 성공", new MemberResponse(member));
    }
}
