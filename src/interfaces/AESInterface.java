package interfaces;

public interface AESInterface {
    static String cbc = "AES/CBC/PKCS5Padding";
    static final String dbKey = "dksl_dlrp_eho???";
    static final String dbIv = dbKey.substring(0, 16);

    static String ofb = "AES/OFB/NoPadding";
    static final String messageKey = "dlrp_dkaghghkwl!";
    static final String messageIv = messageKey.substring(0, 16);
}
