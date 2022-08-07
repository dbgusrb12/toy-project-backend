package com.hg.blog.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SHA256Util {

    private static final String ALGORITHM = "SHA-256";

    // TODO 암호화 salt 키 하드코딩 제거 해야 함.
    private static final String SALT = "encryptSaltKey";

    public static String getEncrypt(String origin) {
        byte[] salt = getSaltByte().getBytes(StandardCharsets.UTF_8);
        return getEncrypt(origin, salt);
    }

    /**
     * SHA-256 암호화 함
     *
     * @param source 원본
     * @return
     */
    public static String getEncrypt(String source, byte[] salt) {
        byte[] sourceBytes = source.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = Arrays.copyOf(sourceBytes, sourceBytes.length + salt.length);
        System.arraycopy(salt, 0, bytes, sourceBytes.length, salt.length);

        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(bytes);

            byte[] hashValue = md.digest();
            return bytesToHex(hashValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("ALGORITHM is not valid");
        }
    }

    private static String bytesToHex(byte[] hashValue) {
        StringBuffer hexString = new StringBuffer();
        for (byte hashByte : hashValue) {
            String hex = Integer.toHexString(hashByte & 0xFF);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String getSaltByte() {
        byte[] salt = SALT.getBytes(StandardCharsets.UTF_8);
        return bytesToHex(salt);
    }
}
