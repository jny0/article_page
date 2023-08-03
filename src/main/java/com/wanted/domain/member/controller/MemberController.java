package com.wanted.domain.member.controller;

import com.wanted.domain.member.dto.JoinResponse;
import com.wanted.domain.member.dto.LoginResponse;
import com.wanted.domain.member.dto.MemberRequest;
import com.wanted.domain.member.service.MemberService;
import com.wanted.global.dto.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody MemberRequest memberRequest){
        ResponseDTO<JoinResponse> response = memberService.join(memberRequest);
        if(response.isFail()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody MemberRequest memberRequest){
        ResponseDTO<LoginResponse> response =  memberService.login(memberRequest);
        if(response.isFail()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
