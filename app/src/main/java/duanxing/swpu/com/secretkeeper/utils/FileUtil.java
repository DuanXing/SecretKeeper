package duanxing.swpu.com.secretkeeper.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import java.io.File;

/**
 * Created by duanxing on 09/05/2017.
 */

/**
 * the tool class of file
 */
public class FileUtil {
    private static final String TAG = "FileUtil";

    /**
     * Get file absolute path by uri
     * @param uri
     * @param context
     * @return
     */
    public static String getRealPath(final Uri uri, final Context context) {
        if(null == uri)
            return null;

        String scheme = uri.getScheme();
        String data = null;
        if(null == scheme)
            data = uri.getPath();
        else if(ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if(ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if(null != cursor) {
                if(cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if(index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }

        return data;
    }

    /**
     * get time stamp for file save name.
     * @return time stamp string
     */
    public static String getTimeStamp() {
        long time = System.currentTimeMillis() / 1000;
        String timeStamp = String.valueOf(time);

        return timeStamp;
    }

    /**
     * judge the file is exists?
     * @param filePath
     * @return return true if exists. else false.
     */
    public static boolean fileExists(final String filePath) {
        try {
            File file = new File(filePath);
            if(!file.exists()) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * delete file
     * @param filePath
     * @return true if success, else false.
     */
    public static boolean deleteFile(final String filePath) {
        try {
            File file = new File(filePath);
            if(!file.delete()) {
                return false;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "file delete failed.");
            return false;
        }

        return true;
    }
}
