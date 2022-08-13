package com.hg.blog.api.post.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.AUTH_HEADER;
import static com.hg.blog.constants.Constants.POST_API;
import static com.hg.blog.constants.Constants.TOKEN_TYPE;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.api.post.service.PostService;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.util.JWTProvider;
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
        PostCreateCommand request = new PostCreateCommand("post1", "content");
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        given(postService.savePost(eq("userId"), any()))
            .willReturn(1L);

        mockMvc.perform(post(API_PREFIX + POST_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTH_HEADER, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body", is(1)));
    }

    @Test
    public void savePostAuthErrorTest() throws Exception {
        PostCreateCommand request = new PostCreateCommand("post1", "content");
        given(postService.savePost(eq("userId"), any()))
            .willReturn(1L);

        mockMvc.perform(post(API_PREFIX + POST_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void savePostNotExistTitleErrorTest() throws Exception {
        PostCreateCommand request = new PostCreateCommand(null, "content");
        Account account = Account.of("userId", "password", "nickname");
        String token = JWTProvider.generateToken(account);
        given(postService.savePost(eq("userId"), any()))
            .willReturn(1L);

        mockMvc.perform(post(API_PREFIX + POST_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTH_HEADER, TOKEN_TYPE + " " + token)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }
}