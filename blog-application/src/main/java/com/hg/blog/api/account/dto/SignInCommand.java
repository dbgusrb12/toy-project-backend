package com.hg.blog.api.account.dto;

import com.hg.blog.util.RSAUtil;
import com.hg.blog.util.SHA256Util;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInCommand {
    @NotBlank(message = "userId 는 필수 값입니다.")
    private String userId;
    @NotBlank(message = "password 는 필수 값입니다.")
    private String password;

    public void passwordEncrypt(RSAUtil rsaUtil) {
        String rsaDecrypt = rsaUtil.decrypt(this.password);
        this.password = SHA256Util.getEncrypt(rsaDecrypt);
    }
}
