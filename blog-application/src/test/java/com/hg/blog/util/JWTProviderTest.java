package com.hg.blog.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.blog.domain.account.entity.Account;
import org.junit.jupiter.api.Test;

public class JWTProviderTest {

    @Test
    public void generateTokenTest() {
        Account account = createAccount();
        String token = JWTProvider.generateToken(account);
        System.out.println("token => " + token);
    }

    @Test
    public void getUserIdFromJWTTest() {
        Account account = createAccount();
        String token = JWTProvider.generateToken(account);
        String userId = JWTProvider.getUserIdFromJWT(token);
        assertThat(userId).isEqualTo(account.getUserId());
    }

    @Test
    public void validateTokenTest() {
        Account account = createAccount();
        String token = JWTProvider.generateToken(account);
        boolean valid = JWTProvider.validateToken(token);
        assertThat(valid).isTrue();
    }


    private Account createAccount() {
        return Account.of("userid", "password", "nickname");
    }
}