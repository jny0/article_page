package com.wanted.domain.member.dto;

import com.wanted.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinResponse {
    private final Member member;

    public static JoinResponse of(Member member){
        return new JoinResponse(member);
    }
}
