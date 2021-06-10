package util;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

public interface Security {
    static String cbc = "AES/CBC/PKCS5Padding";
    static final String dbKey = "dksl_dlrp_eho???";
    static final String dbIv = dbKey.substring(0, 16);

    static String ofb = "AES/OFB/NoPadding";
    static final String messageKey = "dlrp_dkaghghkwl!";
    static final String messageIv = messageKey.substring(0, 16);

}
