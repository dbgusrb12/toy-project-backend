package com.hg.blog.domain.account.service;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

    private final AccountRepository accountRepository;


    public Account signIn(String userId, String password) {
        Optional<Account> signInUser = accountRepository.findByUserIdAndPassword(userId,
            password);
        return signInUser.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }
}
