package com.wanted.member;

import com.wanted.domain.member.entity.Member;
import com.wanted.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
class MemberControllerTests {

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
    @DisplayName("POST /member/join 회원가입 성공")
    void joinSuccessTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .content("""
                                {
                                    "email": "user@example.com",
                                    "password": "password123"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.member.email").value("user@example.com"));

    }

    @Test
    @DisplayName("POST /member/join 회원가입 실패 - 이메일 형식 오류")
    void joinFailTest01() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .content("""
                                {
                                    "email": "user",
                                    "password": "password123"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andDo(print());

        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /member/join 회원가입 실패 - 중복된 이메일")
    void joinFailTest02() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .content("""
                                {
                                    "email": "test@example.com",
                                    "password": "password123"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andDo(print());

        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.resultCode").value("F-1"));
    }


    @Test
    @DisplayName("POST /member/login 로그인 성공")
    void loginSuccessTest() throws Exception {

        System.out.println(testMember.getEmail());
        System.out.println(testMember.getPassword());
        ResultActions resultActions = mvc
                .perform(post("/member/login")
                        .content("""
                                {
                                    "email": "test@example.com",
                                    "password": "password123"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

    @Test
    @DisplayName("POST /member/login 로그인 실패 - 존재하지 않는 회원")
    void loginFailTest01() throws Exception {

        System.out.println(testMember.getEmail());
        System.out.println(testMember.getPassword());
        ResultActions resultActions = mvc
                .perform(post("/member/login")
                        .content("""
                                {
                                    "email": "unknown@example.com",
                                    "password": "password123"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andDo(print());

        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.resultCode").value("F-1"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("POST /member/login 로그인 실패 - 비밀번호 불일치")
    void loginFailTest02() throws Exception {

        System.out.println(testMember.getEmail());
        System.out.println(testMember.getPassword());
        ResultActions resultActions = mvc
                .perform(post("/member/login")
                        .content("""
                                {
                                    "email": "test@example.com",
                                    "password": "WrongPassword"
                                }
                                """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andDo(print());

        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.resultCode").value("F-2"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
