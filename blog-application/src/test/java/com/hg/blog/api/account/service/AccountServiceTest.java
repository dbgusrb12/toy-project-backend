package com.hg.blog.api.account.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.hg.blog.api.account.dto.SignUpCommand;
import com.hg.blog.domain.account.service.AccountCommandService;
import com.hg.blog.util.RSAUtil;
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
    private RSAUtil rsaUtil;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void signUpTest() {
        // given
        SignUpCommand signUpCommand = createSignUpCommand();
        given(rsaUtil.decrypt(any()))
            .willReturn("password");
        // when
        accountService.signUp(signUpCommand);
        // then
        verify(accountCommandService).addAccount(any());
    }

    private SignUpCommand createSignUpCommand() {
        return SignUpCommand.builder()
            .userId("userId")
            .password("password")
            .nickname("nickname")
            .build();
    }

}