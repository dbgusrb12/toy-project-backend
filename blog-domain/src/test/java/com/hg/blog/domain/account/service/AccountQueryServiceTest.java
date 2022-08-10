package com.hg.blog.domain.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountQueryServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountQueryService accountQueryService;

    @Test
    public void signInTest() {
        Account account = createAccount();
        given(accountRepository.findByUserIdAndPassword(account.getUserId(), account.getPassword()))
            .willReturn(Optional.of(account));
        Account signInUser = accountQueryService.signIn(account.getUserId(), account.getPassword());
        assertThat(signInUser.getUserId()).isEqualTo("userid");
        assertThat(signInUser.getPassword()).isEqualTo("password");
        assertThat(signInUser.getNickname()).isEqualTo("nickname");
    }

    @Test
    public void signInErrorTest() {
        Account account = createAccount();
        Executable execute = () -> accountQueryService.signIn(account.getUserId(),
            account.getPassword());
        assertThrows(IllegalArgumentException.class, execute);
    }

    private Account createAccount() {
        return Account.of("userid", "password", "nickname");
    }
}