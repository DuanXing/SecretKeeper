package duanxing.swpu.com.secretkeeper.utils;

import android.util.Log;

/**
 * Created by duanxing on 03/05/2017.
 */

public abstract class BaseEncryption {
    // cipher mode.
    public enum OP_CIPHER_MODE {
        BASE_ENCRYPT_MODE,
        BASE_DECRYPT_MODE
    }

    // cache size
    public static final int CACHE_SIZE = 1024;

    // initial before encrypt
    public abstract boolean init();

    // auto choose encrypt or decrypt
    public boolean doFinal() {
        if(!initialized) {
            Log.e(TAG, "Not initialized.");

            return false;
        }

        if(OP_CIPHER_MODE.BASE_ENCRYPT_MODE == cipherMode) {
            return encrypt();
        }
        else {
            return decrypt();
        }
    }

    // encryption
    protected abstract boolean encrypt();

    // decryption
    protected abstract boolean decrypt();

    // the cipher mode.
    protected OP_CIPHER_MODE cipherMode;

    // tag to the class.
    protected String TAG = "BASE_ENCRYPTION";

    // is initialized ?
    protected boolean initialized = false;

    // source file path
    protected String srcFilePath;

    // save file path
    protected String saveFilePath;
}
