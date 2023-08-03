package com.wanted.article.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private Member testMember;

    @BeforeEach
    void beforeEachTest() {
        testMember = new Member("test@example.com", passwordEncoder.encode("password123"));
        memberRepository.save(testMember);
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = "MEMBER")
    @DisplayName("POST /article/create - 게시글 생성 성공")
    void createSuccessTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/article/create")
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
    @WithMockUser(username = "test1@example.com", authorities = "MEMBER")
    @DisplayName("POST /article/create - 게시글 생성 실패, 존재하지 않는 회원")
    void createFailTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/article/create")
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
}
