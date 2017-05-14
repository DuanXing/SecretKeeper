package duanxing.swpu.com.secretkeeper.utils;

/**
 * Created by duanxing on 14/05/2017.
 */

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * calculate md5 of string or file
 */
public class Md5Calculate {
    private static final String TAG = "Md5Calculate";
    public static boolean isBusy = false;

    /**
     * md5 of string
     * @param str
     * @return
     */
    public static String getMd5OfStr(final String str) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(str.getBytes());

            byte[] bytes = digest.digest();
            BigInteger bigInteger = new BigInteger(1, bytes);
            result = bigInteger.toString(16);
        }
        catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such Algorithm.");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * md5 of file
     * @param filePath
     * @return
     */
    public static String getMd5OfFile(final String filePath) {
        String result = null;

        try {
            File file = new File(filePath);
            FileInputStream in = new FileInputStream(file);
            MappedByteBuffer buffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(buffer);
            in.close();

            byte[] bytes = digest.digest();
            BigInteger bigInteger = new BigInteger(1, bytes);
            result = bigInteger.toString(16);
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "No such file.");
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such Algorithm.");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.e(TAG, "IOException.");
            e.printStackTrace();
        }

        return result;
    }
}
