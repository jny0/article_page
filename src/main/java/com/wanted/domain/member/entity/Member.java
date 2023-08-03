package com.wanted.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanted.global.base.entitiy.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor

public class Member extends BaseEntity {
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;

    public Member(String email, String password){
        this.email = email;
        this.password = password;
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER")); // 회원에게 MEMBER 권한 부여
        return authorities;
    }

    public Map<String, Object> toClaims(){
        return Map.of(
                "id", getId(),
                "email", getEmail()
        );
    }
}
