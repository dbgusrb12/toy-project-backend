package com.hg.blog.domain.account.service;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountCommandService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account addAccount(String userId, String password, String nickname) {
        checkExistAccount(userId);
        Account account = Account.of(userId, password, nickname);
        return accountRepository.save(account);
    }

    private void checkExistAccount(String userId) {
        accountRepository.findByUserId(userId)
            .ifPresent(findAccount -> {
                throw new IllegalArgumentException("이미 존재하는 ID 입니다.");
            });
    }
}
