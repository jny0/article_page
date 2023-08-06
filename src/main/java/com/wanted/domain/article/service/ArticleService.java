package com.wanted.domain.article.service;

import com.wanted.domain.article.dto.ArticleRequest;
import com.wanted.domain.article.dto.ArticleResponse;
import com.wanted.domain.article.entity.Article;
import com.wanted.domain.article.repository.ArticleRepository;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public ResponseDTO<ArticleResponse> create(ArticleRequest articleRequest, Member author){
        Article article = Article.createArticle(articleRequest, author);
        articleRepository.save(article);
        return ResponseDTO.of("S-1", "게시글 등록 완료", ArticleResponse.of(article));
    }

    @Transactional(readOnly = true)
    public ResponseDTO<ArticleResponse> get(Long articleId){
        Article article = findById(articleId).orElse(null);
        if(article == null){
            return ResponseDTO.of("F-1", "존재하지 않는 게시물");
        }
        return ResponseDTO.of("S-1", "게시물 조회 완료", ArticleResponse.of(article));
    }

    @Transactional(readOnly = true)
    public ResponseDTO<List<ArticleResponse>> getList(Pageable pageable){
        Page<Article> articlePage = findAll(pageable);
        return ResponseDTO.of("S-1", "게시물 목록 조회 완료", articlePage.map(ArticleResponse::of).getContent());
    }

    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAllByOrderByCreateDate(pageable);
    }

    @Transactional
    public ResponseDTO<ArticleResponse> update(Article article, ArticleRequest articleRequest){
        article.update(articleRequest);
        return ResponseDTO.of("S-1", "게시글 수정 완료", ArticleResponse.of(article));
    }

    @Transactional
    public ResponseDTO<ArticleResponse> delete(Article article) {
        articleRepository.delete(article);
        return ResponseDTO.of("S-1", "게시글 삭제 완료");
    }

    @Transactional(readOnly = true)
    public ResponseDTO<ArticleResponse> checkValidationAndPermission(Long articleId, Member author){
        Article article = findById(articleId).orElse(null);
        if(article == null){
            return ResponseDTO.of("F-1", "존재하지 않는 게시물");
        }
        if(!Objects.equals(author.getId(), article.getAuthor().getId())){
            return ResponseDTO.of("F-2", "수정 및 삭제 권한 없음");
        }
        return ResponseDTO.of("S-1", "수정 및 삭제 가능", ArticleResponse.of(article));
    }

    public Optional<Article> findById(Long id){
        return articleRepository.findById(id);
    }

}
