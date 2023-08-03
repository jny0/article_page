package com.wanted.domain.article.service;

import com.wanted.domain.article.dto.ArticleRequest;
import com.wanted.domain.article.dto.ArticleResponse;
import com.wanted.domain.article.entity.Article;
import com.wanted.domain.article.repository.ArticleRepository;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public ResponseDTO<ArticleResponse> create(ArticleRequest articleRequest, Member author){
        Article article = Article.createArticle(articleRequest, author);
        articleRepository.save(article);
        return new ResponseDTO<>("S-1", "게시글 등록 완료", new ArticleResponse(article));
    }
}
