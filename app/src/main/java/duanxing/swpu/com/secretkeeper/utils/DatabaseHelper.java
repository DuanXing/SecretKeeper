package duanxing.swpu.com.secretkeeper.utils;

/**
 * Created by duanxing on 14/05/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.util.Calendar;

import javax.crypto.Cipher;

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
    public static final String USER_KEY_YEAR = "year";
    public static final String USER_KEY_MONTH = "month";
    public static final String USER_KEY_DAY = "day";
    public static final String USER_KEY_HOUR = "hour";
    public static final String USER_KEY_MINUTE = "minute";
    public static final String USER_KEY_SECOND = "second";
    public static final String USER_KEY_ERRORTIMES = "errorTimes";

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

    /**
     * 密码错误次数自增1
     */
    public static int addErrorTimes() {
        int t = 0;

        DatabaseHelper databaseHelper = new DatabaseHelper(ContextHolder.getAppContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TB_USER, new String[] {DatabaseHelper.USER_KEY_ERRORTIMES },
                 "id=?", new String[] {"1"}, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            t = cursor.getInt(0);
            ++t;

            DatabaseCipher databaseCipher = new DatabaseCipher(Cipher.ENCRYPT_MODE);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.USER_KEY_ERRORTIMES, t);

            // update value in database
            int i = db.update(DatabaseHelper.TB_USER, contentValues, DatabaseHelper.USER_KEY_NAME + "=?", new String[]{databaseCipher.doFinal(DatabaseHelper.USER_VALUE_NAME)});
            if(i > 0) {
                // success
            }
            else {
                t = -1;
            }

            db.close();
        }
        else {
            db.close();
        }

        return t;
    }

    /**
     * 重置密码错误次数
     */
    public static boolean resetErrorTimes() {
        DatabaseHelper dbHelper = new DatabaseHelper(ContextHolder.getAppContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        DatabaseCipher databaseCipher = new DatabaseCipher(Cipher.ENCRYPT_MODE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_KEY_ERRORTIMES, 0);

        // update value in database
        int i = db.update(DatabaseHelper.TB_USER, contentValues, DatabaseHelper.USER_KEY_NAME + "=?", new String[]{databaseCipher.doFinal(DatabaseHelper.USER_VALUE_NAME)});

        db.close();

        if(i > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 获取密码错误次数
     * @return 密码错误次数
     */
    public static int getErrorTimes() {
        int t = 0;

        DatabaseHelper databaseHelper = new DatabaseHelper(ContextHolder.getAppContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TB_USER, new String[] {DatabaseHelper.USER_KEY_ERRORTIMES },
                "id=?", new String[] {"1"}, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            t = cursor.getInt(0);

            db.close();
        }
        else {
            db.close();
        }

        return t;
    }

    /**
     * 更新下次登录时间
     */
    public static boolean updateNextLoginTime() {
        DatabaseHelper dbHelper = new DatabaseHelper(ContextHolder.getAppContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        DatabaseCipher databaseCipher = new DatabaseCipher(Cipher.ENCRYPT_MODE);

        Calendar calendar = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_KEY_ERRORTIMES, 0);
        contentValues.put(DatabaseHelper.USER_KEY_YEAR, calendar.get(Calendar.YEAR));
        contentValues.put(DatabaseHelper.USER_KEY_MONTH, calendar.get(Calendar.MONTH) + 1);
        contentValues.put(DatabaseHelper.USER_KEY_DAY, calendar.get(Calendar.DAY_OF_MONTH));
        contentValues.put(DatabaseHelper.USER_KEY_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
        contentValues.put(DatabaseHelper.USER_KEY_MINUTE, calendar.get(Calendar.MINUTE));
        contentValues.put(DatabaseHelper.USER_KEY_SECOND, calendar.get(Calendar.SECOND));

        // update value in database
        int i = db.update(DatabaseHelper.TB_USER, contentValues, DatabaseHelper.USER_KEY_NAME + "=?", new String[]{databaseCipher.doFinal(DatabaseHelper.USER_VALUE_NAME)});

        db.close();

        if(i > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 锁定账号24小时
     */
    public static boolean LockUser() {
        DatabaseHelper dbHelper = new DatabaseHelper(ContextHolder.getAppContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        DatabaseCipher databaseCipher = new DatabaseCipher(Cipher.ENCRYPT_MODE);

        Calendar calendar = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_KEY_ERRORTIMES, 0);
        contentValues.put(DatabaseHelper.USER_KEY_YEAR, calendar.get(Calendar.YEAR));
        contentValues.put(DatabaseHelper.USER_KEY_MONTH, calendar.get(Calendar.MONTH) + 1);
        contentValues.put(DatabaseHelper.USER_KEY_DAY, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        contentValues.put(DatabaseHelper.USER_KEY_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
        contentValues.put(DatabaseHelper.USER_KEY_MINUTE, calendar.get(Calendar.MINUTE));
        contentValues.put(DatabaseHelper.USER_KEY_SECOND, calendar.get(Calendar.SECOND));

        // update value in database
        int i = db.update(DatabaseHelper.TB_USER, contentValues, DatabaseHelper.USER_KEY_NAME + "=?", new String[]{databaseCipher.doFinal(DatabaseHelper.USER_VALUE_NAME)});

        db.close();

        if(i > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 获取下次登录的时间
     * @param out
     */
    public static boolean getNextLoginTime(int out[]) {
        DatabaseHelper databaseHelper = new DatabaseHelper(ContextHolder.getAppContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TB_USER, new String[] {DatabaseHelper.USER_KEY_YEAR, DatabaseHelper.USER_KEY_MONTH, DatabaseHelper.USER_KEY_DAY,
                DatabaseHelper.USER_KEY_HOUR, DatabaseHelper.USER_KEY_MINUTE, DatabaseHelper.USER_KEY_SECOND}, "id=?", new String[] {"1"}, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            out[0] = cursor.getInt(0);
            out[1] = cursor.getInt(1);
            out[2] = cursor.getInt(2);
            out[3] = cursor.getInt(3);
            out[4] = cursor.getInt(4);
            out[5] = cursor.getInt(5);

            db.close();

            return true;
        }
        else {
            db.close();

            return false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "create database.");

        // create table user
        String sql = "create table " + TB_USER + "(" + USER_KEY_ID + " integer primary key autoincrement, " + USER_KEY_NAME + " text, " + USER_KEY_PD
                + " text, " + USER_KEY_EMAIL + " text, " + USER_KEY_Q1 + " text, " + USER_KEY_Q2 + " text, " + USER_KEY_Q3 + " text, " + USER_KEY_A1
                + " text, " + USER_KEY_A2 + " text, " + USER_KEY_A3 + " text, " + USER_KEY_ERRORTIMES + " integer, " + USER_KEY_YEAR + " integer, " + USER_KEY_MONTH
                + " integer, " + USER_KEY_DAY + " integer, " + USER_KEY_HOUR + " integer, " + USER_KEY_MINUTE + " integer, " + USER_KEY_SECOND + " integer)";
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
