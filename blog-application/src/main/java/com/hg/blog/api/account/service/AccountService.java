package com.hg.blog.api.account.service;


import com.hg.blog.api.account.dto.SignInCommand;
import com.hg.blog.api.account.dto.SignUpCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountCommandService;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.util.JWTProvider;
import com.hg.blog.util.RSAUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final RSAUtil rsaUtil;
    private final AccountCommandService accountCommandService;
    private final AccountQueryService accountQueryService;

    public void signUp(SignUpCommand request) {
        final Account account = request.passwordEncrypt(rsaUtil).toEntity();
        accountCommandService.addAccount(account);
    }

    public String signIn(SignInCommand request) {
        request.passwordEncrypt(rsaUtil);
        Account account = accountQueryService.signIn(request.getUserId(), request.getPassword());
        return JWTProvider.generateToken(account);
    }
}
