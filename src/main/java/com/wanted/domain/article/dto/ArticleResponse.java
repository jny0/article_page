package com.wanted.domain.article.dto;

import com.wanted.domain.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleResponse {
    private Article article;
}
