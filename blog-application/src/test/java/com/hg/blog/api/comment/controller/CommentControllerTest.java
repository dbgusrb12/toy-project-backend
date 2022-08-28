package com.hg.blog.api.comment.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.COMMENT_API;
import static com.hg.blog.constants.Constants.POST_API;
import static com.hg.blog.constants.Constants.TOKEN_TYPE;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hg.blog.api.comment.dto.CommentDto.CommentCreateCommand;
import com.hg.blog.api.comment.dto.CommentDto.CommentUpdateCommand;
import com.hg.blog.api.comment.service.CommentService;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.util.JWTProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private String getBody(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    public void saveCommentTest() throws Exception {
        // given
        CommentCreateCommand request = new CommentCreateCommand(1L, "content");
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        given(commentService.saveComment(eq("userId"), any()))
            .willReturn(1L);

        // when, then
        mockMvc.perform(post(API_PREFIX + COMMENT_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body", is(1)))
            .andDo(print());
    }

    @Test
    public void saveCommentTest_토큰_없을_경우_에러() throws Exception {
        // given
        CommentCreateCommand request = new CommentCreateCommand(1L, "content");

        // when, then
        mockMvc.perform(post(API_PREFIX + POST_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError())
            .andDo(print());
    }

    @Test
    public void saveCommentTest_postId_없을_경우_에러() throws Exception {
        // given
        CommentCreateCommand request = new CommentCreateCommand(null, "content");
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);

        // when, then
        mockMvc.perform(post(API_PREFIX + COMMENT_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError())
            .andDo(print());
    }

    @Test
    public void saveCommentTest_content_없을_경우_에러() throws Exception {
        // given
        CommentCreateCommand request = new CommentCreateCommand(1L, null);
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);

        // when, then
        mockMvc.perform(post(API_PREFIX + COMMENT_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError())
            .andDo(print());
    }

    @Test
    public void updateCommentTest() throws Exception {
        // given
        long commentId = 1;
        CommentUpdateCommand request = new CommentUpdateCommand("update content");
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        given(commentService.updateComment(eq(commentId), eq("userId"), any()))
            .willReturn(1L);

        // when, then
        mockMvc.perform(put(API_PREFIX + COMMENT_API + "/{commentId}", commentId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body", is(1)))
            .andDo(print());
    }

    @Test
    public void updateCommentTest_토큰_없을_경우_에러() throws Exception {
        // given
        long commentId = 1;
        CommentUpdateCommand request = new CommentUpdateCommand("update content");

        // when, then
        mockMvc.perform(put(API_PREFIX + COMMENT_API + "/{commentId}", commentId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError())
            .andDo(print());
    }

    @Test
    public void updateCommentTest_content_없을_경우_에러() throws Exception {
        // given
        long commentId = 1;
        CommentUpdateCommand request = new CommentUpdateCommand(null);
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);

        // when, then
        mockMvc.perform(put(API_PREFIX + COMMENT_API + "/{commentId}", commentId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError())
            .andDo(print());
    }
}