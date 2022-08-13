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

    @InjectMocks
    private AccountService accountService;

    @Test
    public void signUpTest() {
        // given
        SignUpCommand request = createSignUpCommand();
        // when
        accountService.signUp(request);
        // then
        verify(accountCommandService).saveAccount(request.getUserId(),
            SHA256Util.getEncrypt(request.getPassword()), request.getNickname());
    }

    @Test
    public void signInTest() {
        SignInCommand request = createSignInCommand();
        Account account = createAccount();
        given(accountQueryService.signIn(request.getUserId(),
            SHA256Util.getEncrypt(request.getPassword())))
            .willReturn(account);
        String token = accountService.signIn(request);
        String userId = JWTProvider.getUserIdFromJWT(token);
        boolean valid = JWTProvider.validateToken(token);
        assertThat(token).isNotNull();
        assertThat(userId).isEqualTo(account.getUserId());
        assertThat(valid).isTrue();
    }

    @Test
    public void getRsaPublicKeyTest() {
        String rsaPublicKey = accountService.getRsaPublicKey();
        assertThat(rsaPublicKey).isNotNull();
        assertThat(rsaPublicKey).startsWith("-----BEGIN RSA PUBLIC KEY-----");
        assertThat(rsaPublicKey).endsWith("-----END RSA PUBLIC KEY-----\n");
    }

    private SignUpCommand createSignUpCommand() {
        return new SignUpCommand("userId", "password", "nickname");
    }

    private SignInCommand createSignInCommand() {
        return new SignInCommand("userId", "password");
    }

    private Account createAccount() {
        return Account.of("userId", "password", "nickname");
    }

}