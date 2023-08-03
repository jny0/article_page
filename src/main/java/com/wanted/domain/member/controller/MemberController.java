package com.wanted.domain.member.controller;

import com.wanted.domain.member.dto.JoinResponse;
import com.wanted.domain.member.dto.LoginResponse;
import com.wanted.domain.member.dto.MemberRequest;
import com.wanted.domain.member.service.MemberService;
import com.wanted.global.dto.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseDTO<JoinResponse> join(@Valid @RequestBody MemberRequest memberRequest){
        return memberService.join(memberRequest);
    }

    @PostMapping("/login")
    public ResponseDTO<LoginResponse> login(@Valid @RequestBody MemberRequest memberRequest, HttpServletResponse resp){
        return memberService.login(memberRequest);
    }
}
