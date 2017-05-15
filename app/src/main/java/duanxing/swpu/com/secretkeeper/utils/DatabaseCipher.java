package duanxing.swpu.com.secretkeeper.utils;

import android.util.Log;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by duanxing on 15/05/2017.
 */

/**
 * cipher of database
 */
public class DatabaseCipher {
    private Key aesKey;
    private Cipher aesCipher;

    private static final String TAG = "DatabaseCipher";

    public DatabaseCipher(int mode) {
        // init Key.
        byte[] bKey = EncryptionHelper.AES_KEY.getBytes();
        aesKey = new SecretKeySpec(bKey, "AES");

        // init Cipher.
        try {
            aesCipher = Cipher.getInstance("AES");
            aesCipher.init(mode, aesKey);
        }
        catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "NoSuchAlgorithmException");
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            Log.e(TAG, "NoSuchPaddingException");
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            Log.e(TAG, "InvalidKeyException");
            e.printStackTrace();
        }
    }

    public String doFinal(final String src) {
        String dest = null;
        try {
            final byte[] out = aesCipher.doFinal(src.getBytes());
            dest = new String(out);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return dest;
    }
}
