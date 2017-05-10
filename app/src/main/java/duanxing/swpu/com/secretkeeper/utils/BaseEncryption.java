package duanxing.swpu.com.secretkeeper.utils;

/**
 * Created by duanxing on 03/05/2017.
 */

public abstract class BaseEncryption {
    // cipher mode.
    public enum OP_CIPHER_MODE {
        BASE_ENCRYPT_MODE,
        BASE_DECRYPT_MODE
    }

    // initial before encrypt
    public abstract boolean init();

    // encryption
    public abstract boolean encrypt(String filePath, String savePath);

    // decryption
    public abstract boolean decrypt(String filePath, String savePath);

    // the cipher mode.
    protected OP_CIPHER_MODE cipherMode;

    // tag to the class.
    protected String TAG = "BASE_ENCRYPTION";

    // is initialized ?
    protected boolean initialized = false;
}
