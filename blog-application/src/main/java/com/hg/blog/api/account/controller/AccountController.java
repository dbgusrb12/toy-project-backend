package com.hg.blog.api.account.controller;

import static com.hg.blog.constants.Constants.ACCOUNT_API;
import static com.hg.blog.constants.Constants.API_PREFIX;

import com.hg.blog.api.account.dto.SignInCommand;
import com.hg.blog.api.account.dto.SignUpCommand;
import com.hg.blog.api.account.service.AccountService;
import com.hg.blog.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + ACCOUNT_API)
@Tag(name = "account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    @Operation(description = "회원 가입")
    public Response<Void> signUp(@Valid @RequestBody SignUpCommand request) {
        accountService.signUp(request);
        return Response.ok();
    }

    @PostMapping("/sign-in")
    @Operation(description = "로그인")
    public Response<String> signIn(@Valid @RequestBody SignInCommand request) {
        return Response.of(accountService.signIn(request));
    }

    @GetMapping("/rsa-key")
    @Operation(description = "rsa key 조회")
    public Response<String> getRsaPublicKey() {
        return Response.of(accountService.getRsaPublicKey());
    }
}
