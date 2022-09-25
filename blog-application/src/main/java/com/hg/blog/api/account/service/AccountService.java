package com.hg.blog.api.account.service;


import com.hg.blog.api.account.dto.SignInCommand;
import com.hg.blog.api.account.dto.SignUpCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountCommandService;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.util.JWTProvider;
import com.hg.blog.util.RSAUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountCommandService accountCommandService;
    private final AccountQueryService accountQueryService;
    private final PasswordEncoder bcryptPasswordEncoder;

    @Transactional
    public void signUp(SignUpCommand request) {
        accountCommandService.saveAccount(
            request.getUserId(),
            bcryptPasswordEncoder.encode(request.getPassword()),
            request.getNickname()
        );
    }

    public String signIn(SignInCommand request) {
        final Account account = accountQueryService.getAccountByUserId(request.getUserId());
        checkPasswordMatches(request.getPassword(), account.getPassword());
        return JWTProvider.generateToken(account);
    }

    public String getRsaPublicKey() {
        final RSAUtil rsaUtil = new RSAUtil();
        return rsaUtil.writePublicKeyToString();
    }

    private void checkPasswordMatches(String password, String encodePassword) {
        if (!bcryptPasswordEncoder.matches(password, encodePassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
