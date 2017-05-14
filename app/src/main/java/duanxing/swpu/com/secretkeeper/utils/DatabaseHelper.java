package duanxing.swpu.com.secretkeeper.utils;

/**
 * Created by duanxing on 14/05/2017.
 */

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * helper class of database
 */
public class DatabaseHelper {
    public static final String dbPath = "/data/secretKeeper/secret.db";

    /**
     * 检测数据库db文件是否存在，判断是否使用过秘密记
     * @return
     */
    public static boolean hasNote() {
        try {
            File file = new File(dbPath);
            if(!file.exists()) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }

        return true;
    }
}
