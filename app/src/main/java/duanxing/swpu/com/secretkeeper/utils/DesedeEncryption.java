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

public class DesedeEncryption extends BaseEncryption{
    private Key desedeKey;
    private Cipher desedeCipher;

    // construction.
    public DesedeEncryption(OP_CIPHER_MODE mode) {
        this.cipherMode = mode;
        this.TAG = "DESEDE_ENCRYPTION";
    }

    @Override
    public boolean init() {
        // TODO initialized = true
        // init Key.
        byte[] bKey = EncryptionHelper.DESEDE_KEY.getBytes();
        desedeKey = new SecretKeySpec(bKey, "DESede");

        // init Cipher.
        try {
            desedeCipher = Cipher.getInstance("DESede");
            if(OP_CIPHER_MODE.BASE_ENCRYPT_MODE == this.cipherMode) {
                desedeCipher.init(Cipher.ENCRYPT_MODE, desedeKey);
            } else {
                desedeCipher.init(Cipher.DECRYPT_MODE, desedeKey);
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
    public boolean encrypt(String filePath, String savePath) {
        if(!initialized) {
            Log.i(TAG, "Not initialized.");
            return false;
        }

        if(OP_CIPHER_MODE.BASE_ENCRYPT_MODE != cipherMode) {
            Log.i(TAG, "the cipherMode not in Encryption mode.");
            return false;
        }

        try {
            return encrypt(new FileInputStream(filePath), savePath);
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();
        }

        return false;
    }

    public boolean encrypt(InputStream inFile, String savePath) {
        if(null == inFile) {
            Log.e(TAG, "no inputStream.");
            return false;
        }

        CipherInputStream cipherIn = new CipherInputStream(inFile, desedeCipher);
        try {
            OutputStream outFile = new FileOutputStream(savePath);

            // first, write the file header.
            byte[] fileHeader = EncryptionHelper.DES_FILE_HEADER.getBytes();
            outFile.write(fileHeader, 0, EncryptionHelper.fileHeaderLen);

            // write the encrypted data.
            byte[] fileCache = new byte[1024];
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
    public boolean decrypt(String filePath, String savePath) {
        if(!initialized) {
            Log.i(TAG, "Not initialized.");
            return false;
        }

        if(OP_CIPHER_MODE.BASE_DECRYPT_MODE != cipherMode) {
            Log.i(TAG, "the cipherMode not in Decryption mode.");
            return false;
        }

        try {
            return decrypt(new FileInputStream(filePath), savePath);
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();
        }

        return false;
    }

    public boolean decrypt(InputStream inFile, String savePath) {
        if(null == inFile) {
            Log.e(TAG, "no inputStream.");
            return false;
        }

        // skip the file header.
        try {
            inFile.skip(EncryptionHelper.fileHeaderLen);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        CipherInputStream cipherIn = new CipherInputStream(inFile, desedeCipher);
        try {
            OutputStream outFile = new FileOutputStream(savePath);
            byte[] fileCache = new byte[1024];
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
