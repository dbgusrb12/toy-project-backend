package com.hg.blog.domain.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(AccountQueryService.class)
@ActiveProfiles({"blog-domain", "local"})
public class AccountQueryServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountQueryService accountQueryService;

    @Test
    public void signInTest() {
        // given
        Account account = saveAccount();

        // when
        Account signInUser = accountQueryService.signIn(account.getUserId(), account.getPassword());

        // then
        assertThat(signInUser.getUserId()).isEqualTo("userId");
        assertThat(signInUser.getPassword()).isEqualTo("password");
        assertThat(signInUser.getNickname()).isEqualTo("nickname");
    }

    @Test
    public void signInTest_유저가_존재하지_않을_경우_에러() {
        // given
        String userId = "userId";
        String password = "password";

        // when
        Executable execute = () -> accountQueryService.signIn(userId, password);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 유저입니다.");
    }

    @Test
    public void getAccountByUserIdTest() {
        // given
        Account account = saveAccount();

        // when
        Account findAccount = accountQueryService.getAccountByUserId(account.getUserId());

        // then
        assertThat(findAccount).isNotNull();
        assertThat(findAccount.getUserId()).isEqualTo("userId");
        assertThat(findAccount.getPassword()).isEqualTo("password");
        assertThat(findAccount.getNickname()).isEqualTo("nickname");
    }

    @Test
    public void getAccountByUserIdTest_유저가_존재하지_않을_경우_에러() {
        // given, when
        Executable execute = () -> accountQueryService.getAccountByUserId("userId");

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 유저입니다.");
    }

    private Account saveAccount() {
        Account account = Account.of("userId", "password", "nickname");
        return accountRepository.save(account);
    }
}