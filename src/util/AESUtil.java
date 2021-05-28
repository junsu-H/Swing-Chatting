package util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESUtil {
    static String algorithm = "AES/CBC/PKCS5Padding";
    static IvParameterSpec ivParameterSpec = AESUtil.generateIv();

//    static {
//        try {
//            key = AESUtil.generateKey(128);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(n);
//        SecretKey key = keyGenerator.generateKey();
//        return key;
//    }

    /* 임의로 secret key 넣기 */
    public static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }


    /* Test */
    static void givenStringWhenEncryptThenSuccess()
            throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        String password = "junsu";
        String cipherText = AESUtil.encrypt(password);
        String plainText = AESUtil.decrypt(cipherText);
        if (password.equals(plainText)){
            System.out.println("같다고 말해!");
        }
    }

    /* Test */
    void givenPassword_whenEncrypt_thenSuccess()
            throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        // given
        String plainText = "www.baeldung.com";
        String password = "baeldung";
        String salt = "12345678";
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.getKeyFromPassword(password, salt);

        // when
        String cipherText = AESUtil.encryptPasswordBased(plainText, key, ivParameterSpec);
        String decryptedCipherText = AESUtil.decryptPasswordBased(cipherText, key, ivParameterSpec);

        // then
        if (password.equals(plainText)){
            System.out.println("같다고 말해!");
        }

    }


    public static String encryptPasswordBased(String plainText, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return Base64.getEncoder()
                .encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public static String decryptPasswordBased(String cipherText, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(Base64.getDecoder()
                .decode(cipherText)));
    }


    public static void main(String[] args) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        AESUtil.givenStringWhenEncryptThenSuccess();
    }
}