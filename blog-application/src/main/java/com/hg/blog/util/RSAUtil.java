package com.hg.blog.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RSAUtil {

    private final String ALGORITHM = "RSA";
    private final String CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private final String RSA_FOLDER = "rsa";
    private final String PRIVATE_KEY_PATH = "/id_rsa";
    private final String PRIVATE_KEY_BYTE_PATH = "/private-key";
    private final String PUBLIC_KEY_PATH = "/id_rsa.pub";
    private final String PUBLIC_KEY_BYTE_PATH = "/public-key";
    private final String PRIVATE_DESCRIPTION = "RSA PRIVATE KEY";
    private final String PUBLIC_DESCRIPTION = "RSA PUBLIC KEY";

    private String privateKey;

    private String publicKey;

    @PostConstruct
    public void init() {
        PrivateKey privateKey;
        PublicKey publicKey;
        try {
            byte[] privateKeyFromFile = getPrivateKeyFromFile();
            byte[] publicKeyFromFile = getPublicKeyFromFile();
            privateKey = getPrivateKey(privateKeyFromFile);
            publicKey = getPublicKey(publicKeyFromFile);
        } catch (Exception e) {
            KeyPair keyPair = this.createKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }
        writePrivateKeyFile(privateKey);
        savePrivateKeyAsFile(privateKey);
        writePublicKeyFile(publicKey);
        savePublicKeyAsFile(publicKey);
        this.privateKey = privateKeyToBase64(privateKey);
        this.publicKey = publicKeyToBase64(publicKey);
    }

    /**
     * 문자열을 암호화한 후 Base64로 encoding 하여 반환한다.
     *
     * @param strToEncrypt 암호화할 문자열
     * @return Base64로 변환된 암호화된 문자열
     */
    public String encrypt(String strToEncrypt) {
        Cipher cipher = getCipherInstance(Cipher.ENCRYPT_MODE, base64ToPublicKey());
        try {
            byte[] encryptedByte = cipher.doFinal(strToEncrypt.getBytes());
            return base64Encode(encryptedByte); // Base64 변환
        } catch (IllegalBlockSizeException e) {
            log.error("encrypt error ==> {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (BadPaddingException e) {
            log.error("encrypt error ==> {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    /**
     * Base64로 인코딩된 암호화된 문자열을 문자열로 변환한다.
     *
     * @param strToDecrypt Base64로 인코딩된 암호화된 문자열
     * @return 복호화된 문자열
     */
    public String decrypt(String strToDecrypt) {
        Cipher cipher = getCipherInstance(Cipher.DECRYPT_MODE, base64ToPrivateKey());
        try {
            byte[] encryptedByte = base64Decode(strToDecrypt.getBytes()); // Base64 decoding
            byte[] decryptedByte = cipher.doFinal(encryptedByte);
            return new String(decryptedByte);
        } catch (IllegalBlockSizeException e) {
            log.error("encrypt error ==> {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (BadPaddingException e) {
            log.error("encrypt error ==> {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Cipher getCipherInstance(int mode, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(mode, key);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error("get cipher instance error ==> {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("get cipher instance error ==> {}", e.getMessage());
            throw new IllegalArgumentException("key is not valid");
        }
    }

    // Key Section

    /**
     * 공개키/개인키 KeyPair 생성
     *
     * @return
     */
    private KeyPair createKeyPair() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
//            keyPairGen.initialize(2048);
            keyPairGen.initialize(1024);
            return keyPairGen.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("create key pair error ==> {}", e.getMessage());
            throw new IllegalArgumentException("ALGORITHM is not valid");
        }
    }

    /**
     * 개인키의 byte[]로 개인키를 생성
     *
     * @param keyBytes 개인키의 바이트배열
     * @return 개인키
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    private PrivateKey getPrivateKey(byte[] keyBytes) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            log.error("get private key error ==> {}", e.getMessage());
            throw new IllegalArgumentException("ALGORITHM is not valid");
        } catch (InvalidKeySpecException e) {
            log.error("get private key error ==> {}", e.getMessage());
            throw new IllegalArgumentException("key space is not valid");
        }
    }

    /**
     * 공개키의 byte[]로 공개키를 생성
     *
     * @param keyBytes 공개키의 바이트 배열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Exception
     */
    private PublicKey getPublicKey(byte[] keyBytes) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            log.error("get public key error ==> {}", e.getMessage());
            throw new IllegalArgumentException("ALGORITHM is not valid");
        } catch (InvalidKeySpecException e) {
            log.error("get public key error ==> {}", e.getMessage());
            throw new IllegalArgumentException("key space is not valid");
        }
    }

    /**
     * 개인키를 Base64로 encoding 하여 반환
     *
     * @param privateKey PrivateKey 인스턴스
     * @return Base64로 인코딩된 문자열
     */
    private String privateKeyToBase64(PrivateKey privateKey) {
        byte[] keyBytes = privateKey.getEncoded();
        return base64Encode(keyBytes);
    }

    /**
     * 공개키를 Base64로 encoding 하여 반환
     *
     * @param publicKey PublicKey 인스턴스
     * @return Base64로 인코딩된 문자열
     */
    private String publicKeyToBase64(PublicKey publicKey) {
        byte[] keyBytes = publicKey.getEncoded();
        return base64Encode(keyBytes);
    }

    /**
     * Base64로 인코딩된 문자열을 PrivateKey 로 변환
     *
     * @return PrivateKey 인스턴스
     */
    private PrivateKey base64ToPrivateKey() {
        byte[] decodedByte = base64Decode(this.privateKey.getBytes());
        return getPrivateKey(decodedByte);
    }

    /**
     * Base64로 인코딩된 문자열을 PublicKey 로 변환
     *
     * @return PublicKey 인스턴스
     */
    private PublicKey base64ToPublicKey() {
        byte[] decodedByte = base64Decode(this.publicKey.getBytes());
        return getPublicKey(decodedByte);
    }

    private String base64Encode(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

    private byte[] base64Decode(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    // utility

    /**
     * 공개키를 파일로 저장 (classpath:rsa/public-key)
     *
     * @param publicKey
     */
    private void savePublicKeyAsFile(PublicKey publicKey) {
        final String rsaFolderAbsolutePath = getRsaFolderAbsolutePath();
        saveKeyAsFile(publicKey.getEncoded(), rsaFolderAbsolutePath + PUBLIC_KEY_BYTE_PATH);
    }

    /**
     * 개인키를 파일로 저장 (classpath:rsa/private-key)
     *
     * @param privateKey
     */
    private void savePrivateKeyAsFile(PrivateKey privateKey) {
        final String rsaFolderAbsolutePath = getRsaFolderAbsolutePath();
        saveKeyAsFile(privateKey.getEncoded(), rsaFolderAbsolutePath + PRIVATE_KEY_BYTE_PATH);
    }

    /**
     * 공개키/개인키를 파일로 저장.
     *
     * @param keyBytes 키의 바이트배열
     * @param filePath 저장할 파일명(풀패스)
     */
    private void saveKeyAsFile(byte[] keyBytes, String filePath) {
        // 파일 시스템에 암호화된 공개키를 쓴다.
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(keyBytes);
        } catch (IOException e) {
            log.error("save key as file error ==> {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 공개키를 파일로 부터 읽어들인다 (classpath:rsa/public-key)
     *
     * @throws Exception
     */
    private byte[] getPrivateKeyFromFile() throws Exception {
        return getKeyFromFile(PRIVATE_KEY_BYTE_PATH);
    }

    /**
     * 개인키를 파일로 부터 읽어들인다 (classpath:rsa/private-key)
     *
     * @throws Exception
     */
    private byte[] getPublicKeyFromFile() throws Exception {
        return getKeyFromFile(PUBLIC_KEY_BYTE_PATH);
    }

    /**
     * 공개키/개인키를 파일로 부터 읽어들인다.
     *
     * @param filePath 저장된 파일명
     * @return 키의 바이트배열
     */
    private byte[] getKeyFromFile(String filePath) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(RSA_FOLDER + filePath);
        byte[] keyBytes = classPathResource.getInputStream().readAllBytes();
        return keyBytes;
    }

    /**
     * 개인키를 PEM 파일형식으로 저장 (classpath:rsa/id_rsa)
     *
     * @param privateKey
     * @throws IOException
     */
    private void writePrivateKeyFile(PrivateKey privateKey) {
        final String rsaFolderAbsolutePath = getRsaFolderAbsolutePath();
        writePemFile(privateKey, PRIVATE_DESCRIPTION, rsaFolderAbsolutePath + PRIVATE_KEY_PATH);
    }

    /**
     * 공개키를 PEM 파일형식으로 저장 (classpath:rsa/id_rsa.pub)
     *
     * @param publicKey
     * @throws IOException
     */
    private void writePublicKeyFile(PublicKey publicKey) {
        final String rsaFolderAbsolutePath = getRsaFolderAbsolutePath();
        writePemFile(publicKey, PUBLIC_DESCRIPTION, rsaFolderAbsolutePath + PUBLIC_KEY_PATH);
    }

    private String getRsaFolderAbsolutePath() {
        try {
            ClassPathResource classPathResource = new ClassPathResource(RSA_FOLDER);
            return classPathResource.getURI().getPath();
        } catch (IOException e) {
            log.error("getRsaFolderAbsolutePath error ==> {}", e.getMessage());
            throw new IllegalArgumentException("url is not valid");
        }
    }

    /**
     * 공개키 또는 개인키를 PEM 파일형식으로 저장
     *
     * @param key         개인키 또는 공개키
     * @param description 키 구분자. "RSA PRIVATE KEY" 또는 "RSA PUBLIC KEY"
     * @param filename    저장할 파일명
     */
    private void writePemFile(Key key, String description, String filename) {
        PemObject pemObject = new PemObject(description, key.getEncoded());
        try (PemWriter pemWriter = new PemWriter(
            new OutputStreamWriter(new FileOutputStream(filename)))) {
            pemWriter.writeObject(pemObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String writePublicKeyToString() {
        PublicKey publicKey = base64ToPublicKey();
        return writePemToString(publicKey, PUBLIC_DESCRIPTION);
    }

    public String writePrivateKeyToString() {
        PrivateKey privateKey = base64ToPrivateKey();
        return writePemToString(privateKey, PRIVATE_DESCRIPTION);
    }

    /**
     * 공개키 또는 개인키를 PEM 형식으로 변환하여 반환
     *
     * @param key         공개키 또는 개인키
     * @param description 키 구분자. "RSA PRIVATE KEY" 또는 "RSA PUBLIC KEY"
     * @return PEM 형식 문자열
     */
    private String writePemToString(Key key, String description) {
        PemObject pemObject = new PemObject(description, key.getEncoded());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(byteArrayOutputStream))) {
            pemWriter.writeObject(pemObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toString();
    }
}
