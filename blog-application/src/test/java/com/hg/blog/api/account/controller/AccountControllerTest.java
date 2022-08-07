package com.hg.blog.api.account.controller;

import static com.hg.blog.constants.Constants.ACCOUNT_API;
import static com.hg.blog.constants.Constants.API_PREFIX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hg.blog.api.account.dto.AccountDto;
import com.hg.blog.api.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
        AccountDto.SignUpCommand request = AccountDto.SignUpCommand.builder()
            .userId("userId")
            .password("password")
            .nickname("nickname")
            .build();

        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-up")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().isOk());
    }

    @Test
    public void signUpNotUserIdError() throws Exception {
        AccountDto.SignUpCommand request = AccountDto.SignUpCommand.builder()
            .password("password")
            .nickname("nickname")
            .build();

        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-up")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void signUpNotPasswordError() throws Exception {
        AccountDto.SignUpCommand request = AccountDto.SignUpCommand.builder()
            .userId("userId")
            .nickname("nickname")
            .build();

        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-up")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void signUpNotNicknameError() throws Exception {
        AccountDto.SignUpCommand request = AccountDto.SignUpCommand.builder()
            .userId("userId")
            .password("password")
            .build();

        mockMvc.perform(post(API_PREFIX + ACCOUNT_API + "/sign-up")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getBody(request)))
            .andExpect(status().is4xxClientError());
    }

}