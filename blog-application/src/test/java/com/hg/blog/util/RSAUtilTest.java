package com.hg.blog.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RSAUtil.class)
public class RSAUtilTest {

    @Autowired
    private RSAUtil rsaUtil;

    @Test
    public void rsaEncryptAndDecrypt() throws Exception {
        String originStr = "sample text";
        String encrypt = rsaUtil.encrypt(originStr);
        String decrypt = rsaUtil.decrypt(encrypt);
        assertThat(originStr).isEqualTo(decrypt);
    }
}