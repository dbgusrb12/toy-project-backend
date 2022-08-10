package com.hg.blog.api.account.controller;

import static com.hg.blog.constants.Constants.ACCOUNT_API;
import static com.hg.blog.constants.Constants.API_PREFIX;

import com.hg.blog.api.account.dto.SignInCommand;
import com.hg.blog.api.account.dto.SignUpCommand;
import com.hg.blog.api.account.service.AccountService;
import com.hg.blog.response.Response;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + ACCOUNT_API)
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public Response signUp(@Valid @RequestBody SignUpCommand request) {
        accountService.signUp(request);
        return Response.ok();
    }

    @PostMapping("/sign-in")
    public Response<String> signIn(@Valid @RequestBody SignInCommand request) {
        return Response.of(accountService.signIn(request));
    }
}
