package com.wanted.domain.member.dto;

import com.wanted.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse{
    private final Member member;
}
