package com.wanted.domain.article.controller;

import com.wanted.domain.article.entity.Article;
import com.wanted.domain.article.repository.ArticleRepository;
import com.wanted.domain.member.entity.Member;
import com.wanted.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ArticleControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;
    private Member testMember;
    private Article testArticle;

    @BeforeEach
    void beforeEachTest() {
        testMember = new Member("test@example.com", passwordEncoder.encode("password123"));
        memberRepository.save(testMember);
        memberRepository.save(new Member("test2@example.com", passwordEncoder.encode("password123")));
        testArticle = new Article("테스트제목", "테스트내용", testMember);
        articleRepository.save(testArticle);
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = "MEMBER")
    @DisplayName("POST /article - 게시글 생성 성공")
    void createSuccessTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/article")
                        .content("""
                                {
                                    "title": "제목",
                                    "content": "내용"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                        .andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.article.title").value("제목"))
                .andExpect(jsonPath("$.data.article.content").value("내용"))
        ;
    }

    @Test
    @WithMockUser(username = "unknown@example.com", authorities = "MEMBER")
    @DisplayName("POST /article - 게시글 생성 실패, 존재하지 않는 회원")
    void createFailTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/article")
                        .content("""
                                {
                                    "title": "제목",
                                    "content": "내용"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andDo(print());

        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.resultCode").value("E-RuntimeException"))
        ;
    }

    @Test
    @DisplayName("GET /article/ - 게시글 목록 조회 성공")
    void getListSuccessTest() throws Exception {
        Article testArticle2 = new Article("테스트제목2", "테스트내용2", testMember);
        Article testArticle3 = new Article("테스트제목3", "테스트내용3", testMember);
        articleRepository.save(testArticle2);
        articleRepository.save(testArticle3);

        ResultActions resultActions = mvc
                .perform(get("/article"))
                .andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].article.id").value(testArticle.getId()))
                .andExpect(jsonPath("$.data[0].article.title").value(testArticle.getTitle()))
                .andExpect(jsonPath("$.data[0].article.content").value(testArticle.getContent()))
                .andExpect(jsonPath("$.data[0].authorId").value(testArticle.getAuthor().getId()));
        ;
    }

    @Test
    @DisplayName("GET /article/{id} - 게시글 단건 조회 성공")
    void showDetailSuccessTests() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/article/%s".formatted(testArticle.getId())))
                .andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.article.id").value(testArticle.getId()));
        ;
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = "MEMBER")
    @DisplayName("PATCH /article/{id} - 게시글 수정 성공")
    void updateSuccessTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(patch("/article/%s".formatted(testArticle.getId()))
                        .content("""
                                {
                                    "title": "제목 수정",
                                    "content": "내용 수정"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.article.title").value("제목 수정"))
                .andExpect(jsonPath("$.data.article.content").value("내용 수정"))
        ;
    }

    @Test
    @WithMockUser(username = "test2@example.com", authorities = "MEMBER")
    @DisplayName("PATCH /article/{id} - 게시글 수정 실패 - 권한없음")
    void updateFailTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(patch("/article/%s".formatted(testArticle.getId()))
                        .content("""
                                {
                                    "title": "제목 수정",
                                    "content": "내용 수정"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andDo(print());

        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.resultCode").value("F-2"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist())
        ;
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = "MEMBER")
    @DisplayName("DELETE /article/{id} - 게시글 삭제 성공")
    void deleteSuccessTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(delete("/article/%s".formatted(testArticle.getId())))
                .andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist())
        ;
    }

    @Test
    @WithMockUser(username = "test2@example.com", authorities = "MEMBER")
    @DisplayName("DELETE /article/{id} - 게시글 삭제 실패 - 권한없음")
    void deleteFailTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(delete("/article/%s".formatted(testArticle.getId())))
                .andDo(print());

        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.resultCode").value("F-2"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist())
        ;
    }
}
