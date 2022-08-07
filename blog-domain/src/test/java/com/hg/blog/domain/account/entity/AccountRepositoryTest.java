package com.hg.blog.domain.account.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void save_account_test() {
        // given, when
        Account savedAccount = saveAccount();
        // then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getUserId()).isEqualTo("userid");
        assertThat(savedAccount.getPassword()).isEqualTo("password");
        assertThat(savedAccount.getNickname()).isEqualTo("nickname");
    }

    @Test
    public void find_by_userid_test() {
        // given
        Account account = saveAccount();
        // when
        Account findAccount = accountRepository.findByUserId(account.getUserId()).get();

        // then
        assertThat(findAccount).isNotNull();
        assertThat(findAccount.getUserId()).isEqualTo("userid");
        assertThat(findAccount.getPassword()).isEqualTo("password");
        assertThat(findAccount.getNickname()).isEqualTo("nickname");
    }


    private Account saveAccount() {
        Account account = Account.of("userid", "password", "nickname");
        Account savedAccount = accountRepository.save(account);
        return savedAccount;
    }
}