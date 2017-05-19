package duanxing.swpu.com.secretkeeper.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import javax.crypto.Cipher;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.DatabaseCipher;
import duanxing.swpu.com.secretkeeper.utils.DatabaseHelper;

public class NoteLoginActivity extends BaseActivity {
    private Button btn_login;
    private Button btn_forget;
    private Button btn_recover;
    private EditText ed_password;
    private TextView txt_hint;
    private int errorTimes = 0;

    private boolean needSelectAll = false;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_note_login);
    }

    @Override
    protected void initSubView() {
        btn_forget = (Button) findViewById(R.id.forget);
        btn_login = (Button) findViewById(R.id.login);
        btn_recover = (Button) findViewById(R.id.recover);
        ed_password = (EditText) findViewById(R.id.password);
        txt_hint = (TextView) findViewById(R.id.loginHint);

        // 测试功能
//        btn_recover.setAlpha(0);
//        btn_recover.setEnabled(false);
    }

    @Override
    protected void setListener() {
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                enterActivity(GetBackPasswordActivity.class);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPd = ed_password.getText().toString();
                if("".equals(strPd)) {
                    txt_hint.setText(getResources().getString(R.string.inputPass));
                    return;
                }

                int date[] = new int[6];
                int now[] = new int[6];
                Calendar calendar = Calendar.getInstance();
                now[0] = calendar.get(Calendar.YEAR);
                now[1] = calendar.get(Calendar.MONTH);
                now[2] = calendar.get(Calendar.DAY_OF_MONTH);
                now[3] = calendar.get(Calendar.HOUR_OF_DAY);
                now[4] = calendar.get(Calendar.MINUTE);
                now[5] = calendar.get(Calendar.SECOND);
                if(!DatabaseHelper.getNextLoginTime(date)) {
                    txt_hint.setText("获取数据库时间失败!");
                    return;
                }
                if(!isDateBeforeNow(date, now)) {
                    txt_hint.setText("您的账号将在 " + date[0] + "年" + date[1] + "月" + date[2] + "日 " + date[3] + ":" + date[4] + ":" + date[5] + " 后解锁!");
                    return;
                }


                // encrypt password
                DatabaseCipher databaseCipher = new DatabaseCipher(Cipher.ENCRYPT_MODE);
                strPd = databaseCipher.doFinal(strPd);

                // get password from database
                DatabaseHelper databaseHelper = new DatabaseHelper(NoteLoginActivity.this);
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                Cursor cursor = db.query(DatabaseHelper.TB_USER, new String[] {DatabaseHelper.USER_KEY_PD}, "id=?", new String[] {"1"}, null, null, null);
                if(cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String str = cursor.getString(0);
                    if(str.equals(strPd)) {
                        if(!DatabaseHelper.updateNextLoginTime()) {
                            txt_hint.setText("更新数据库时间失败!");
                        }
                        finish();
                        enterActivity(NoteListActivity.class);
                    }
                    else {
                        errorTimes = DatabaseHelper.addErrorTimes();
                        if(5 == errorTimes) {
                            if(DatabaseHelper.LockUser()) {
                                txt_hint.setText("你的账号已被锁定!");
                            }
                            else {
                                txt_hint.setText("账号锁定失败！");
                            }
                        }
                        else if(-1 == errorTimes) {
                            txt_hint.setText("更新错误次数失败!");
                        }
                        else {
                            txt_hint.setText(getResources().getString(R.string.error) + ", 你还可以尝试" + (5 - errorTimes) + "次。");
                        }
                        needSelectAll = true;
                    }
                }
                else {
                    txt_hint.setText(getResources().getString(R.string.dbError));
                    needSelectAll = true;
                }

                db.close();
            }
        });

        ed_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(needSelectAll) {
                    ed_password.selectAll();
                    needSelectAll = false;
                }
            }
        });

        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                enterActivity(VerifyCodeDialogActivity.class);
            }
        });
    }

    /**
     * 判断现在的时间是否比date晚
     * @return
     */
    private boolean isDateBeforeNow(int[] date, int[] now) {
        if(date[0] > now[0]) {
            return false;
        }
        if(date[1] > now[1]) {
            return false;
        }
        if(date[2] > now[2]) {
            return false;
        }
        if(date[3] > now[3]) {
            return false;
        }
        if(date[4] > now[4]) {
            return false;
        }
        if(date[5] > now[5]) {
            return false;
        }

        return true;
    }

    private void enterActivity(Class<?> cls) {
        Intent intent = new Intent(NoteLoginActivity.this, cls);
        startActivity(intent);
    }

    @Override
    protected void processLogic() {

    }
}
