package interfaces;

public interface AESInterface {
    String cbc = "AES/CBC/PKCS5Padding";
    String dbKey = "dksl_dlrp_eho???";
    String dbIv = dbKey.substring(0, 16);

    String ofb = "AES/OFB/NoPadding";
    String messageKey = "dlrp_dkaghghkwl!";
    String messageIv = messageKey.substring(0, 16);
}
