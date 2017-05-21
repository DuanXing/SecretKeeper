package duanxing.swpu.com.secretkeeper.utils;

/**
 * Created by duanxing on 10/05/2017.
 */

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * helper class of Encrytion
 */
public class EncryptionHelper {
    // key of encryption.
    public static final String DES_KEY = "M=2cOx0q";
    public static final String AES_KEY = "Lq5Y/sQudP5idjREjRmfVM6dWdMJrPtS";
    public static final String DESEDE_KEY = "guY5Z8BE0S9Kbs2K/y62h1vX";

    // file header of encryption.
    public static final String DES_FILE_HEADER = "FdNKmfob2Sua7GDmtkudo+CbbLlg==";
    public static final String AES_FILE_HEADER = "wcU+TjirSafiRusgG0FNYcwPoTE47/";
    public static final String DESEDE_FILE_HEADER = "LS34iRmH160pYtg4Fqa1gVDr3nobXx";
    public static final String SM4_FILE_HEADER = "hp5eHwIkjZgIshfU4FDw4i8zIyG3ec";

    // the length of file header
    public static final int fileHeaderLen = 30;

    // is encryption or decryption busy ?
    public static boolean encryptState = false;
    public static boolean decryptState = false;

    /**
     * get file header from encrypted data.
     * @param filePath
     * @return file header
     */
    public static final String getFileHeader(final String filePath) {
        String strFileHeader = null;
        try {
            InputStream inFile = new FileInputStream(filePath);

            byte[] fileHeader = new byte[fileHeaderLen];
            inFile.read(fileHeader, 0, fileHeaderLen);
            strFileHeader = new String(fileHeader);

            inFile.close();
//            Log.i("EncryptionHelper", "fileHeaderLen:" + fileHeader.length);
//            Log.i("EncryptionHelper", "fileHeader:" + fileHeader);
//            Log.i("EncryptionHelper", "fileHeader:" + strFileHeader);
        }
        catch (Exception e) {
            Log.e("EncryptionHelper", "File not found.");
            e.printStackTrace();
        }

        return strFileHeader;
    }

    /**
     * judge the file is encrypted by SM4 ?
     * @param filePath
     * @return
     */
    public static final boolean isSM4File(final String filePath) {
        try {
            File file = new File(filePath);

            long len = file.length();
            if(14 != len % 16) {
                return false;
            }

            long skipLen = len - 16;

            InputStream inFile = new FileInputStream(filePath);
            inFile.skip(skipLen);

            byte[] srcBuf = new byte[16];
            byte[] buffer = new byte[16];
            inFile.read(srcBuf, 0, 16);
            inFile.close();

            SM4Encryption sm4Encryption = new SM4Encryption(null, null, BaseEncryption.OP_CIPHER_MODE.BASE_DECRYPT_MODE);
            sm4Encryption.init();
            sm4Encryption.singleDecrypt(srcBuf, 16, buffer);

            byte extraData = buffer[15];
            // extraData belong [0, 16]
            if(extraData > 16) {
                return false;
            }

            for(int i = 0;i < extraData;++i) {
                if(extraData != buffer[15 - i]) {
                    return false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
