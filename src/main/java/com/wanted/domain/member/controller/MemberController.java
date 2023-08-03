package com.wanted.domain.member.controller;

import com.wanted.domain.member.dto.JoinRequest;
import com.wanted.domain.member.dto.MemberResponse;
import com.wanted.domain.member.service.MemberService;
import com.wanted.global.dto.ResponseDTO;
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
    public ResponseDTO<MemberResponse> join(@Valid @RequestBody JoinRequest joinRequest){
        return memberService.join(joinRequest);
    }

    @PostMapping("/login")
    public ResponseDTO<MemberResponse> login(@Valid @RequestBody JoinRequest joinRequest){
        return memberService.join(joinRequest);
    }
}
