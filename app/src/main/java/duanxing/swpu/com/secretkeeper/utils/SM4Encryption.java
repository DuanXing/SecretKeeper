package duanxing.swpu.com.secretkeeper.utils;

import android.util.Log;

/**
 * Created by duanxing on 03/05/2017.
 */

public class SM4Encryption extends BaseEncryption{
    public SM4Encryption(OP_CIPHER_MODE mode) {
        this.cipherMode = mode;
        this.TAG = "SM4_ENCRYPTION";
    }

    @Override
    public boolean init() {
        // TODO initialized = true

        return false;
    }

    @Override
    public boolean encrypt(String filePath, String savePath) {
        if(!initialized) {
            Log.i(TAG, "Not initialized.");
            return false;
        }

        if(OP_CIPHER_MODE.BASE_ENCRYPT_MODE != cipherMode) {
            Log.i(TAG, "the cipherMode not in Encryption mode.");
            return false;
        }

        return false;
    }

    @Override
    public boolean decrypt(String filePath, String savePath) {
        if(!initialized) {
            Log.i(TAG, "Not initialized.");
            return false;
        }

        if(OP_CIPHER_MODE.BASE_DECRYPT_MODE != cipherMode) {
            Log.i(TAG, "the cipherMode not in Decryption mode.");
            return false;
        }

        return false;
    }
}
