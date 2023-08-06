package com.wanted.domain.article.dto;

import com.wanted.domain.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleResponse {
    private Long authorId;
    private Article article;

    public static ArticleResponse of(Article article){
        return new ArticleResponse(article.getAuthor().getId(), article);
    }
}
