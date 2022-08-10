package com.hg.blog.api.account.service;

import com.hg.blog.api.account.dto.AccountDto;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountCommandService;
import com.hg.blog.util.RSAUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountCommandService accountCommandService;
    private final RSAUtil rsaUtil;

    public void signUp(AccountDto.SignUpCommand request) {
        final Account account = request.passwordEncrypt(rsaUtil).toEntity();
        accountCommandService.addAccount(account);
    }
}
