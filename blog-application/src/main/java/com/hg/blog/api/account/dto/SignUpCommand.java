package com.hg.blog.api.account.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpCommand {

    @NotBlank(message = "필수 값입니다.")
    private String userId;
    @NotBlank(message = "필수 값입니다.")
    private String password;
    @NotBlank(message = "필수 값입니다.")
    private String nickname;
}