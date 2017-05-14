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
    public static boolean isConnectDb = false;
    private static boolean hasDbfile = false;
    private static DatabaseHelper instance = null;

    // private construction
    private DatabaseHelper() {

    }

    // singleton
    private static DatabaseHelper getInstance() {
        if(null == instance) {
            instance = new DatabaseHelper();
        }

        return instance;
    }

    /**
     * the db file exists ?
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

        hasDbfile = true;
        return true;
    }

    /**
     * Initialize db if need and establish connect.
     */
    private static void initDb() {

    }

    /**
     * connect the db
     */
    public static void openDb() {

    }

    /**
     * close the db connection
     */
    public static void closeDb() {

    }

    /**
     * execute sql
     */
    public static void execSQL(String sql) {

    }
}
