package com.wanted.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;

    public static LoginResponse of(String accessToken){
        return new LoginResponse(accessToken);
    }
}
