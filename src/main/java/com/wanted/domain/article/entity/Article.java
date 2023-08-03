package com.wanted.domain.article.entity;

import com.wanted.domain.article.dto.ArticleRequest;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.base.entitiy.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Article extends BaseEntity {
    @NotNull
    String title;
    @NotNull
    @Column(columnDefinition = "TEXT")
    String content;
    @ManyToOne(fetch = FetchType.LAZY)
    Member author;

    public static Article createArticle(ArticleRequest articleRequest, Member member) {
        return Article.builder()
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .author(member)
                .build();
    }
}
