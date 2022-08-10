package com.hg.blog.api.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.hg.blog.api.account.dto.SignInCommand;
import com.hg.blog.api.account.dto.SignUpCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountCommandService;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.util.JWTProvider;
import com.hg.blog.util.RSAUtil;
import com.hg.blog.util.SHA256Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountCommandService accountCommandService;

    @Mock
    private AccountQueryService accountQueryService;

    @Mock
    private RSAUtil rsaUtil;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void signUpTest() {
        // given
        SignUpCommand request = createSignUpCommand();
        given(rsaUtil.decrypt(any()))
            .willReturn("password");
        // when
        accountService.signUp(request);
        // then
        verify(accountCommandService).addAccount(any());
    }

    @Test
    public void signInTest() {
        SignInCommand request = createSignInCommand();
        Account account = createAccount();
        given(rsaUtil.decrypt(any()))
            .willReturn("password");
        given(accountQueryService.signIn(request.getUserId(), SHA256Util.getEncrypt(request.getPassword())))
            .willReturn(account);
        String token = accountService.signIn(request);
        String userId = JWTProvider.getUserIdFromJWT(token);
        boolean valid = JWTProvider.validateToken(token);
        assertThat(token).isNotNull();
        assertThat(userId).isEqualTo(account.getUserId());
        assertThat(valid).isTrue();
    }

    private SignUpCommand createSignUpCommand() {
        return SignUpCommand.builder()
            .userId("userId")
            .password("password")
            .nickname("nickname")
            .build();
    }

    private SignInCommand createSignInCommand() {
        return SignInCommand.builder()
            .userId("userId")
            .password("password")
            .build();
    }

    private Account createAccount() {
        return Account.of("userId", "password", "nickname");
    }

}