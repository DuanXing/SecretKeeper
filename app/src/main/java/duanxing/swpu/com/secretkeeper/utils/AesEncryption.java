package duanxing.swpu.com.secretkeeper.utils;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by duanxing on 03/05/2017.
 */

public class AesEncryption extends BaseEncryption{
    private Key aesKey;
    private Cipher aesCipher;

    // construction
    public AesEncryption(String src, String dest, OP_CIPHER_MODE mode) {
        this.cipherMode = mode;
        this.TAG = "AES_ENCRYPTION";
        this.srcFilePath = src;
        this.saveFilePath = dest;
    }

    @Override
    public boolean init() {
        // TODO initialized = true
        // init Key.
        byte[] bKey = EncryptionHelper.AES_KEY.getBytes();
        aesKey = new SecretKeySpec(bKey, "AES");

        // init Cipher.
        try {
            aesCipher = Cipher.getInstance("AES");
            if(OP_CIPHER_MODE.BASE_ENCRYPT_MODE == this.cipherMode) {
                aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
            } else {
                aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
            }

            initialized = true;

            return true;
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

        return false;
    }

    @Override
    protected boolean encrypt() {
        if(!initialized) {
            Log.i(TAG, "Not initialized.");
            return false;
        }

        if(OP_CIPHER_MODE.BASE_ENCRYPT_MODE != cipherMode) {
            Log.i(TAG, "the cipherMode not in Encryption mode.");
            return false;
        }

        try {
            InputStream inFile = new FileInputStream(srcFilePath);
            if(null == inFile) {
                Log.e(TAG, "no inputStream.");
                return false;
            }

            CipherInputStream cipherIn = new CipherInputStream(inFile, aesCipher);
            OutputStream outFile = new FileOutputStream(saveFilePath);

            // first, write the file header.
            byte[] fileHeader = EncryptionHelper.AES_FILE_HEADER.getBytes();
            outFile.write(fileHeader, 0, EncryptionHelper.fileHeaderLen);

            // write the encrypted data.
            byte[] fileCache = new byte[CACHE_SIZE];
            int len;
            while ((len = cipherIn.read(fileCache)) > 0) {
                outFile.write(fileCache, 0, len);
                outFile.flush();
            }
            inFile.close();
            outFile.close();
            cipherIn.close();

            Log.i(TAG, "Encrypt success!");

            return true;
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();

            Log.e(TAG, "Encrypt failed!");
        }
        catch (IOException e) {
            Log.e(TAG, "IOException");
            e.printStackTrace();

            Log.e(TAG, "Encrypt failed!");
        }

        return false;
    }

    @Override
    protected boolean decrypt() {
        if(!initialized) {
            Log.i(TAG, "Not initialized.");
            return false;
        }

        if(OP_CIPHER_MODE.BASE_DECRYPT_MODE != cipherMode) {
            Log.i(TAG, "the cipherMode not in Decryption mode.");
            return false;
        }

        try {
            InputStream inFile = new FileInputStream(srcFilePath);
            if(null == inFile) {
                Log.e(TAG, "no inputStream.");
                return false;
            }

            // skip the file header.
            inFile.skip(EncryptionHelper.fileHeaderLen);

            CipherInputStream cipherIn = new CipherInputStream(inFile, aesCipher);
            OutputStream outFile = new FileOutputStream(saveFilePath);
            byte[] fileCache = new byte[CACHE_SIZE];
            int len;
            while ((len = cipherIn.read(fileCache)) > 0) {
                outFile.write(fileCache, 0, len);
                outFile.flush();
            }
            inFile.close();
            outFile.close();
            cipherIn.close();

            Log.i(TAG, "Decrypt success!");

            return true;
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();

            Log.e(TAG, "Decrypt failed!");
        }
        catch (IOException e) {
            Log.e(TAG, "IOException");
            e.printStackTrace();

            Log.e(TAG, "Decrypt failed!");
        }

        return false;
    }
}
