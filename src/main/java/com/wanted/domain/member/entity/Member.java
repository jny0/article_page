package com.wanted.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanted.global.base.entitiy.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
