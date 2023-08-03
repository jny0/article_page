package com.wanted.domain.article.controller;

import com.wanted.domain.article.dto.ArticleRequest;
import com.wanted.domain.article.dto.ArticleResponse;
import com.wanted.domain.article.service.ArticleService;
import com.wanted.domain.member.entity.Member;
import com.wanted.domain.member.service.MemberService;
import com.wanted.global.dto.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final MemberService memberService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody ArticleRequest articleRequest, @AuthenticationPrincipal User user){
        Member author = memberService.findByEmail(user.getUsername()).orElseThrow();
        ResponseDTO<ArticleResponse> response =  articleService.create(articleRequest, author);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<?> showDetail(@PathVariable Long articleId){
        ResponseDTO<ArticleResponse> response =  articleService.get(articleId);
        if(response.isFail()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
