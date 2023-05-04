package com.jonas.UnlockIT;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESECBCrypto {
    private static final String ALGORITHM = "AES";
    private static final String MODE = "AES/ECB/NoPadding";

    public static byte[] encrypt(byte[] plaintext, byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] cipherText = cipher.doFinal(plaintext);

        return cipherText;
    }

    public static byte[] decrypt(byte[] ciphertext, byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(ciphertext);
    }
}
