package com.hg.blog.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


public class SHA256UtilTest {

    @Test
    public void encrypt_test() {
        String sample = "asdasd";
        String encrypt = SHA256Util.getEncrypt(sample);
        System.out.println(encrypt);
    }

    @Test
    public void decrypt_test() {
        String sample = "amwianclanc";
        String encrypt = SHA256Util.getEncrypt(sample);
        String decrypt = SHA256Util.getEncrypt(sample);
        assertThat(encrypt).isEqualTo(decrypt);
    }
}