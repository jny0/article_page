package com.wanted.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinRequest {
    @NotBlank
    @Pattern(regexp = ".*@.*", message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;
}
