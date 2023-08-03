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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public ResponseDTO<ArticleResponse> create(ArticleRequest articleRequest, Member author){
        Article article = Article.createArticle(articleRequest, author);
        articleRepository.save(article);
        return ResponseDTO.of("S-1", "게시글 등록 완료", new ArticleResponse(article));
    }

    @Transactional(readOnly = true)
    public ResponseDTO<ArticleResponse> get(Long articleId){
        Article article = findById(articleId).orElse(null);
        if(article == null){
            return ResponseDTO.of("F-1", "존재하지 않는 게시물");
        }
        return ResponseDTO.of("S-1", "게시물 조회 완료", new ArticleResponse(article));
    }

    public Optional<Article> findById(Long id){
        return articleRepository.findById(id);
    }


}
