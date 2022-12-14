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
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.core.io.ClassPathResource;

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

    public RSAUtil() {
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
            writePrivateKeyFile(privateKey);
            savePrivateKeyAsFile(privateKey);
            writePublicKeyFile(publicKey);
            savePublicKeyAsFile(publicKey);
        }
        this.privateKey = privateKeyToBase64(privateKey);
        this.publicKey = publicKeyToBase64(publicKey);
    }

    /**
     * ???????????? ???????????? ??? Base64??? encoding ?????? ????????????.
     *
     * @param strToEncrypt ???????????? ?????????
     * @return Base64??? ????????? ???????????? ?????????
     */
    public String encrypt(String strToEncrypt) {
        Cipher cipher = getCipherInstance(Cipher.ENCRYPT_MODE, base64ToPublicKey());
        try {
            byte[] encryptedByte = cipher.doFinal(strToEncrypt.getBytes());
            return base64Encode(encryptedByte); // Base64 ??????
        } catch (IllegalBlockSizeException e) {
            log.error("encrypt error ==> {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (BadPaddingException e) {
            log.error("encrypt error ==> {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    /**
     * Base64??? ???????????? ???????????? ???????????? ???????????? ????????????.
     *
     * @param strToDecrypt Base64??? ???????????? ???????????? ?????????
     * @return ???????????? ?????????
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
     * ?????????/????????? KeyPair ??????
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
     * ???????????? byte[]??? ???????????? ??????
     *
     * @param keyBytes ???????????? ???????????????
     * @return ?????????
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
     * ???????????? byte[]??? ???????????? ??????
     *
     * @param keyBytes ???????????? ????????? ??????
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
     * ???????????? Base64??? encoding ?????? ??????
     *
     * @param privateKey PrivateKey ????????????
     * @return Base64??? ???????????? ?????????
     */
    private String privateKeyToBase64(PrivateKey privateKey) {
        byte[] keyBytes = privateKey.getEncoded();
        return base64Encode(keyBytes);
    }

    /**
     * ???????????? Base64??? encoding ?????? ??????
     *
     * @param publicKey PublicKey ????????????
     * @return Base64??? ???????????? ?????????
     */
    private String publicKeyToBase64(PublicKey publicKey) {
        byte[] keyBytes = publicKey.getEncoded();
        return base64Encode(keyBytes);
    }

    /**
     * Base64??? ???????????? ???????????? PrivateKey ??? ??????
     *
     * @return PrivateKey ????????????
     */
    private PrivateKey base64ToPrivateKey() {
        byte[] decodedByte = base64Decode(this.privateKey.getBytes());
        return getPrivateKey(decodedByte);
    }

    /**
     * Base64??? ???????????? ???????????? PublicKey ??? ??????
     *
     * @return PublicKey ????????????
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
     * ???????????? ????????? ?????? (classpath:rsa/public-key)
     *
     * @param publicKey
     */
    private void savePublicKeyAsFile(PublicKey publicKey) {
        final String rsaFolderAbsolutePath = getRsaFolderAbsolutePath();
        saveKeyAsFile(publicKey.getEncoded(), rsaFolderAbsolutePath + PUBLIC_KEY_BYTE_PATH);
    }

    /**
     * ???????????? ????????? ?????? (classpath:rsa/private-key)
     *
     * @param privateKey
     */
    private void savePrivateKeyAsFile(PrivateKey privateKey) {
        final String rsaFolderAbsolutePath = getRsaFolderAbsolutePath();
        saveKeyAsFile(privateKey.getEncoded(), rsaFolderAbsolutePath + PRIVATE_KEY_BYTE_PATH);
    }

    /**
     * ?????????/???????????? ????????? ??????.
     *
     * @param keyBytes ?????? ???????????????
     * @param filePath ????????? ?????????(?????????)
     */
    private void saveKeyAsFile(byte[] keyBytes, String filePath) {
        // ?????? ???????????? ???????????? ???????????? ??????.
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(keyBytes);
        } catch (IOException e) {
            log.error("save key as file error ==> {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * ???????????? ????????? ?????? ??????????????? (classpath:rsa/public-key)
     *
     * @throws Exception
     */
    private byte[] getPrivateKeyFromFile() throws Exception {
        return getKeyFromFile(PRIVATE_KEY_BYTE_PATH);
    }

    /**
     * ???????????? ????????? ?????? ??????????????? (classpath:rsa/private-key)
     *
     * @throws Exception
     */
    private byte[] getPublicKeyFromFile() throws Exception {
        return getKeyFromFile(PUBLIC_KEY_BYTE_PATH);
    }

    /**
     * ?????????/???????????? ????????? ?????? ???????????????.
     *
     * @param filePath ????????? ?????????
     * @return ?????? ???????????????
     */
    private byte[] getKeyFromFile(String filePath) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(RSA_FOLDER + filePath);
        byte[] keyBytes = classPathResource.getInputStream().readAllBytes();
        return keyBytes;
    }

    /**
     * ???????????? PEM ?????????????????? ?????? (classpath:rsa/id_rsa)
     *
     * @param privateKey
     * @throws IOException
     */
    private void writePrivateKeyFile(PrivateKey privateKey) {
        final String rsaFolderAbsolutePath = getRsaFolderAbsolutePath();
        writePemFile(privateKey, PRIVATE_DESCRIPTION, rsaFolderAbsolutePath + PRIVATE_KEY_PATH);
    }

    /**
     * ???????????? PEM ?????????????????? ?????? (classpath:rsa/id_rsa.pub)
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
     * ????????? ?????? ???????????? PEM ?????????????????? ??????
     *
     * @param key         ????????? ?????? ?????????
     * @param description ??? ?????????. "RSA PRIVATE KEY" ?????? "RSA PUBLIC KEY"
     * @param filename    ????????? ?????????
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
     * ????????? ?????? ???????????? PEM ???????????? ???????????? ??????
     *
     * @param key         ????????? ?????? ?????????
     * @param description ??? ?????????. "RSA PRIVATE KEY" ?????? "RSA PUBLIC KEY"
     * @return PEM ?????? ?????????
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
