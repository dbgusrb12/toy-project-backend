package com.hg.blog.api.account.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hg.blog.api.account.dto.AccountDto;
import com.hg.blog.domain.account.service.AccountCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountCommandService accountCommandService;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void signUpTest() {
        AccountDto.SignUpCommand signUpCommand = createSignUpCommand();
        accountService.signUp(signUpCommand);
        verify(accountCommandService).addAccount(any());
    }

    private AccountDto.SignUpCommand createSignUpCommand() {
        return AccountDto.SignUpCommand.builder()
            .userId("userId")
            .password("password")
            .nickname("nickname")
            .build();
    }

}