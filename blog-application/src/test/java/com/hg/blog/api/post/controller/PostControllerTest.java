package com.hg.blog.api.post.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.POST_API;
import static com.hg.blog.constants.Constants.TOKEN_TYPE;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hg.blog.api.post.dto.PostDto.GetPost;
import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.api.post.dto.PostDto.PostUpdateCommand;
import com.hg.blog.api.post.service.PostService;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.util.JWTProvider;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private String getBody(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    public void savePostTest() throws Exception {
        // given
        PostCreateCommand request = new PostCreateCommand("post1", "content");
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        given(postService.savePost(eq("userId"), any()))
            .willReturn(1L);

        // when, then
        mockMvc.perform(post(API_PREFIX + POST_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body", is(1)));
    }

    @Test
    public void savePostAuthErrorTest() throws Exception {
        // given
        PostCreateCommand request = new PostCreateCommand("post1", "content");

        // when, then
        mockMvc.perform(post(API_PREFIX + POST_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void savePostNotExistTitleErrorTest() throws Exception {
        // given
        PostCreateCommand request = new PostCreateCommand(null, "content");
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        given(postService.savePost(eq("userId"), any()))
            .willReturn(1L);

        // when, then
        mockMvc.perform(post(API_PREFIX + POST_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void updatePostTest() throws Exception {
        // given
        PostUpdateCommand request = new PostUpdateCommand("title", "content");
        long postId = 1;
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        given(postService.updatePost(eq(postId), eq("userId"), any()))
            .willReturn(postId);

        // when, then
        mockMvc.perform(put(API_PREFIX + POST_API + "/{postId}", postId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body", is(1)));
    }

    @Test
    public void updatePostNotAuthErrorTest() throws Exception {
        // given
        PostUpdateCommand request = new PostUpdateCommand("title", "content");
        long postId = 1;

        // when, then
        mockMvc.perform(put(API_PREFIX + POST_API + "/{postId}", postId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void updatePostNotExistTitleErrorTest() throws Exception {
        // given
        PostUpdateCommand request = new PostUpdateCommand(null, "content");
        long postId = 1;
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        given(postService.updatePost(eq(postId), eq("userId"), any()))
            .willReturn(postId);

        // when, then
        mockMvc.perform(put(API_PREFIX + POST_API + "/{postId}", postId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void updatePostNotExistContentErrorTest() throws Exception {
        // given
        PostUpdateCommand request = new PostUpdateCommand("title", null);
        long postId = 1;
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        given(postService.updatePost(eq(postId), eq("userId"), any()))
            .willReturn(postId);

        // when, then
        mockMvc.perform(put(API_PREFIX + POST_API + "/{postId}", postId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void deletePostTest() throws Exception {
        // given
        long postId = 1;
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        willDoNothing().given(postService).deletePost(postId, "userId");

        // when, then
        mockMvc.perform(delete(API_PREFIX + POST_API + "/{postId}", postId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN_TYPE + " " + token))
            .andExpect(status().isOk());
    }

    @Test
    public void deletePostNotAuthTest() throws Exception {
        // given
        long postId = 1;

        // when, then
        mockMvc.perform(put(API_PREFIX + POST_API + "/{postId}", postId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void getPostTest() throws Exception {
        // given
        long postId = 1;
        GetPost getPost = GetPost.of(1, "title", "content", "nickname", LocalDateTime.now(), LocalDateTime.now());
        given(postService.getPost(postId))
            .willReturn(getPost);

        // when, then
        mockMvc.perform(get(API_PREFIX + POST_API + "/{postId}", postId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body.id", is(1)))
            .andExpect(jsonPath("$.body.title", is("title")))
            .andExpect(jsonPath("$.body.content", is("content")))
            .andExpect(jsonPath("$.body.nickname", is("nickname")));
    }


}