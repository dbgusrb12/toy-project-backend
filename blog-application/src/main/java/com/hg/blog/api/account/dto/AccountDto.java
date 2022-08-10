package com.hg.blog.api.account.dto;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.util.RSAUtil;
import com.hg.blog.util.SHA256Util;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountDto {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpCommand {

        @NotBlank(message = "userId 는 필수 값입니다.")
        private String userId;
        @NotBlank(message = "password 는 필수 값입니다.")
        private String password;
        @NotBlank(message = "nickname 은 필수 값입니다.")
        private String nickname;

        public SignUpCommand passwordEncrypt(RSAUtil rsaUtil) {
            String rsaDecrypt = rsaUtil.decrypt(this.password);
            this.password = SHA256Util.getEncrypt(rsaDecrypt);
            return this;
        }

        public Account toEntity() {
            return Account.of(userId, password, nickname);
        }
    }
}
