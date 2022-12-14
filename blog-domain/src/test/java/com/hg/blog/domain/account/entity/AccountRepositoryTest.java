package com.hg.blog.domain.account.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles({"blog-domain", "local"})
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void saveAccountTest() {
        // given, when
        Account savedAccount = saveAccount();

        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getUserId()).isEqualTo("userId");
        assertThat(savedAccount.getPassword()).isEqualTo("password");
        assertThat(savedAccount.getNickname()).isEqualTo("nickname");
    }

    @Test
    public void findByUserIdTest() {
        // given
        Account account = saveAccount();

        // when
        Optional<Account> byId = accountRepository.findByUserId(account.getUserId());

        // then
        assertThat(byId.isPresent()).isTrue();
        Account findAccount = byId.get();
        assertThat(findAccount.getUserId()).isEqualTo("userId");
        assertThat(findAccount.getPassword()).isEqualTo("password");
        assertThat(findAccount.getNickname()).isEqualTo("nickname");
    }

    private Account saveAccount() {
        Account account = Account.of("userId", "password", "nickname");
        return accountRepository.save(account);
    }
}