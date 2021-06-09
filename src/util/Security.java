package util;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

public interface Security {
    static String algorithm = "AES/CBC/PKCS5Padding";
    static final String dbKey = "dksl_dlrp_eho???";
    static final String dbIv = dbKey.substring(0, 16);

    static IvParameterSpec messageIv = AESUtil.generateIv();

}
