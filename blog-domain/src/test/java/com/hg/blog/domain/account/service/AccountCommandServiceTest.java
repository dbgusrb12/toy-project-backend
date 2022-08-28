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
@Import(AccountCommandService.class)
@ActiveProfiles({"blog-domain", "local"})
public class AccountCommandServiceTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountCommandService accountCommandService;

    @Test
    public void saveAccountTest() {
        // given
        String userId = "userId";
        String password = "password";
        String nickname = "nickname";

        // when
        Account savedAccount = accountCommandService.saveAccount(userId, password, nickname);

        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getUserId()).isEqualTo("userId");
        assertThat(savedAccount.getPassword()).isEqualTo("password");
        assertThat(savedAccount.getNickname()).isEqualTo("nickname");
    }

    @Test
    public void saveAccountTest_아이디가_이미_존재할_경우_에러() {
        // given
        String userId = "userId";
        String password = "password";
        String nickname = "nickname";
        createAccount(userId);

        // when
        Executable execute = () -> accountCommandService.saveAccount(userId, password, nickname);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 ID 입니다.");
    }

    private Account createAccount(String userId) {
        Account account = Account.of(userId, "password", "nickname");
        return accountRepository.save(account);
    }
}