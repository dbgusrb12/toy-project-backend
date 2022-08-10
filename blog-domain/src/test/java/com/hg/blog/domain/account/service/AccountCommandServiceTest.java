package com.hg.blog.domain.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
public class AccountCommandServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountCommandService accountCommandService;

    @Test
    public void addAccountTest() {
        // given
        Account account = createAccount();
        given(accountRepository.save(any())).willReturn(account);

        // when
        Account savedAccount = accountCommandService.saveAccount(account.getUserId(),
            account.getPassword(), account.getNickname());

        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getUserId()).isEqualTo("userid");
        assertThat(savedAccount.getPassword()).isEqualTo("password");
        assertThat(savedAccount.getNickname()).isEqualTo("nickname");
    }

    @Test
    public void addAccountExistErrorTest() {
        // given
        Account account = createAccount();
        given(accountRepository.findByUserId(account.getUserId())).willReturn(Optional.of(account));
        // when
        Executable execute = () -> accountCommandService.saveAccount(account.getUserId(),
            account.getPassword(), account.getNickname());
        // then
        assertThrows(IllegalArgumentException.class, execute);
    }

    private Account createAccount() {
        return Account.of("userid", "password", "nickname");
    }
}