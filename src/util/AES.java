package util;

import interfaces.AESInterface;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES implements AESInterface {
    /* Ref:
     * https://www.baeldung.com/java-aes-encryption-decryption
     */

    public static String encrypt(String algorithm, String iv, String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        Cipher cipher = Cipher.getInstance(algorithm);

        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes("UTF-8"));

        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String algorithm, String iv, String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        byte[] plainText = new byte[0];
        try {
            plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(plainText, "UTF-8");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return String.valueOf(plainText);
    }

    /* Test */
    static void givenStringWhenEncryptThenSuccess() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NoSuchFieldException, IllegalAccessException {
        String password = "junsu";
        String cipherText = AES.encrypt(AES.cbc, dbIv, password);
        String plainText = AES.decrypt(AES.cbc, dbIv, cipherText);
        if (password.equals(plainText)) {
            System.out.println("DB 같다고 말해!");
        }
    }

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NoSuchFieldException, IllegalAccessException {
        AES.givenStringWhenEncryptThenSuccess();
    }
}

