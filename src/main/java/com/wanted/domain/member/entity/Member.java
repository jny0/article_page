package com.wanted.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanted.global.base.entitiy.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Map<String, Object> toClaims(){
        return Map.of(
                "id", getId(),
                "email", getEmail()
        );
    }
}
