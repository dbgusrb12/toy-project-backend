package com.hg.blog.api.account.service;


import com.hg.blog.api.account.dto.SignInCommand;
import com.hg.blog.api.account.dto.SignUpCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountCommandService;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.util.JWTProvider;
import com.hg.blog.util.RSAUtil;
import com.hg.blog.util.SHA256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountCommandService accountCommandService;
    private final AccountQueryService accountQueryService;

    public void signUp(SignUpCommand request) {
        accountCommandService.saveAccount(request.getUserId(),
            passwordEncrypt(request.getPassword()), request.getNickname());
    }

    public String signIn(SignInCommand request) {
        Account account = accountQueryService.signIn(request.getUserId(),
            passwordEncrypt(request.getPassword()));
        return JWTProvider.generateToken(account);
    }

    public String getRsaPublicKey() {
        RSAUtil rsaUtil = new RSAUtil();
        return rsaUtil.writePublicKeyToString();
    }

    private String passwordEncrypt(String password) {
        return SHA256Util.getEncrypt(password);
    }
}
