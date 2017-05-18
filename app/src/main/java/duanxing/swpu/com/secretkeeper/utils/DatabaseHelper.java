package duanxing.swpu.com.secretkeeper.utils;

/**
 * Created by duanxing on 14/05/2017.
 */

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;

/**
 * helper class of database
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    // database file path
    private static final String dbPath = "/data/data/duanxing.swpu.com.secretkeeper/databases/secret.db";

    private static Integer version = 1;

    private static final String TAG = "DatabaseHelper";

    // database table
    public static final String TB_USER = "user";
    public static final String TB_NOTE = "note";

    // key of table user
    public static final String USER_KEY_ID = "id";
    public static final String USER_KEY_NAME = "name";
    public static final String USER_VALUE_NAME = "SecretKeeper";
    public static final String USER_KEY_PD = "password";
    public static final String USER_KEY_EMAIL = "email";
    public static final String USER_KEY_Q1 = "question1";
    public static final String USER_KEY_Q2 = "question2";
    public static final String USER_KEY_Q3 = "question3";
    public static final String USER_KEY_A1 = "answer1";
    public static final String USER_KEY_A2 = "answer2";
    public static final String USER_KEY_A3 = "answer3";

    // key of table note
    public static final String NOTE_KEY_ID = "id";
    public static final String NOTE_KEY_TITLE = "title";
    public static final String NOTE_KEY_CONTENT = "content";

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    private DatabaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    private DatabaseHelper(Context context, String name) {
        this(context, name, null, version);
    }

    public DatabaseHelper(Context context) {
        this(context, dbPath, null, version);
    }

    /**
     * the db file exists ?
     * @return
     */
    public static boolean hasNote() {
        try {
            File file = new File(dbPath);
            if(!file.exists()) {
                Log.e(TAG, "db file not exists.");
                return false;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "hasNote catch an exception.");
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public static void deleteDatabase() {
        File file = new File(dbPath);
        if(file.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                SQLiteDatabase.deleteDatabase(file);
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "create database.");

        // create table user
        String sql = "create table " + TB_USER + "(" + USER_KEY_ID + " integer primary key autoincrement, " + USER_KEY_NAME + " text, " + USER_KEY_PD
                + " text, " + USER_KEY_EMAIL + " text, " + USER_KEY_Q1 + " text, " + USER_KEY_Q2 + " text, " + USER_KEY_Q3 + " text, " + USER_KEY_A1
                + " text, " + USER_KEY_A2 + " text, " + USER_KEY_A3 + " text)";
        db.execSQL(sql);

        // create table note
        sql = "create table " + TB_NOTE + "(" + NOTE_KEY_ID + " integer primary key autoincrement, " + NOTE_KEY_TITLE
                + " text, " + NOTE_KEY_CONTENT + " text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "upgrade database to " + newVersion);
    }
}
