package com.hg.blog.api.account.controller;

import static com.hg.blog.constants.Constants.ACCOUNT_API;
import static com.hg.blog.constants.Constants.API_PREFIX;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hg.blog.api.account.dto.SignInCommand;
import com.hg.blog.api.account.dto.SignUpCommand;
import com.hg.blog.api.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private String getBody(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }


    @Test
    public void signUpTest() throws Exception {
        // given
        SignUpCommand request = new SignUpCommand("userId", "password", "nickname");

        // when, then
        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-up")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().isOk());
    }

    @Test
    public void signUpNotUserIdError() throws Exception {
        // given
        SignUpCommand request = new SignUpCommand(null, "password", "nickname");

        // when, then
        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-up")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void signUpNotPasswordError() throws Exception {
        // given
        SignUpCommand request = new SignUpCommand("userId", null, "nickname");

        // when, then
        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-up")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void signUpNotNicknameError() throws Exception {
        // given
        SignUpCommand request = new SignUpCommand("userId", "password", null);

        // when, then
        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-up")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void signInTest() throws Exception {
        // given
        SignInCommand request = new SignInCommand("userId", "password");
        given(accountService.signIn(any()))
            .willReturn("jwtTokenValue");

        // when, then
        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-in")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body", is("jwtTokenValue")));
    }

    @Test
    public void signInNotUserIdErrorTest() throws Exception {
        // given
        SignInCommand request = new SignInCommand(null, "password");

        // when, then
        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-in")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void signInNotPasswordErrorTest() throws Exception {
        // given
        SignInCommand request = new SignInCommand("userId", null);

        // when, then
        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-in")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void getRsaPublicKeyTest() throws Exception {
        // given
        given(accountService.getRsaPublicKey())
            .willReturn("rsaPublicKey");

        // when, then
        mockMvc.perform(get(API_PREFIX + ACCOUNT_API + "/rsa-key")
                .content(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body", is("rsaPublicKey")));
    }

}