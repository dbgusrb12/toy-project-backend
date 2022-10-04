package com.hg.blog.api.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpCommand {

    @NotBlank(message = "필수 값입니다")
    @Schema(description = "유저 아이디")
    private String userId;
    @NotBlank(message = "필수 값입니다")
    @Schema(description = "유저 비밀번호")
    private String password;
    @NotBlank(message = "필수 값입니다")
    @Schema(description = "유저 닉네임")
    private String nickname;
}