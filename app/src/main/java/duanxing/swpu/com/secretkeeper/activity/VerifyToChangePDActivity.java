package duanxing.swpu.com.secretkeeper.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.crypto.Cipher;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.DatabaseCipher;
import duanxing.swpu.com.secretkeeper.utils.DatabaseHelper;
import duanxing.swpu.com.secretkeeper.utils.EmailSender;

public class VerifyToChangePDActivity extends BaseActivity {
    private Button btn_sendCode;
    private Button btn_ensure;
    private EditText ed_pd1;
    private EditText ed_pd2;
    private TimeCount timeCount;
    private EditText ed_verifyCode;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_verify_to_change_pd);
    }

    @Override
    protected void initSubView() {
        ed_pd1 = (EditText) findViewById(R.id.ed_pd1);
        ed_pd2 = (EditText) findViewById(R.id.ed_pd2);
        btn_ensure = (Button) findViewById(R.id.btn_ensure);
        btn_sendCode = (Button) findViewById(R.id.btn_sendCode);
        ed_verifyCode = (EditText) findViewById(R.id.ed_verifyCode);

        timeCount = new TimeCount(60 * 1000, 1000);
    }

    @Override
    protected void setListener() {
        btn_sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EmailSender().execute();
                timeCount.start();
            }
        });

        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断密码是否合法
                String strPd1 = ed_pd1.getText().toString();
                if("".equals(strPd1)) {
                    Toast.makeText(VerifyToChangePDActivity.this, getResources().getString(R.string.noPD1), Toast.LENGTH_SHORT).show();
                    return;
                }
                String strPd2 = ed_pd2.getText().toString();
                if("".equals(strPd2)) {
                    Toast.makeText(VerifyToChangePDActivity.this, getResources().getString(R.string.noPD2), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!strPd1.equals(strPd2)) {
                    Toast.makeText(VerifyToChangePDActivity.this, getResources().getString(R.string.PDinvalid), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strPd1.length() < 8) {
                    Toast.makeText(VerifyToChangePDActivity.this, getResources().getString(R.string.pdLenError), Toast.LENGTH_SHORT).show();
                    return;
                }

                String strCode = ed_verifyCode.getText().toString();
                if("".equals(strCode)) {
                    Toast.makeText(VerifyToChangePDActivity.this, getResources().getString(R.string.invalidCode), Toast.LENGTH_SHORT).show();
                    return;
                }
                int code = Integer.valueOf(strCode);

                if(code == EmailSender.verifyCode && 0 != EmailSender.verifyCode) {
                    DatabaseHelper dbHelper = new DatabaseHelper(VerifyToChangePDActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    DatabaseCipher databaseCipher = new DatabaseCipher(Cipher.ENCRYPT_MODE);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.USER_KEY_PD, databaseCipher.doFinal(strPd1));

                    // update value in database
                    int i = db.update(DatabaseHelper.TB_USER, contentValues, DatabaseHelper.USER_KEY_NAME + "=?", new String[]{databaseCipher.doFinal(DatabaseHelper.USER_VALUE_NAME)});
                    if(i > 0) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.getPdSuccess), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.getPdFailed), Toast.LENGTH_LONG).show();
                    }

                    db.close();

                    finish();
                }
                else {
                    Toast.makeText(VerifyToChangePDActivity.this, "验证码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void processLogic() {

    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_sendCode.setEnabled(false);
            btn_sendCode.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            btn_sendCode.setText("重新发送验证码");
            btn_sendCode.setEnabled(true);
        }
    }
}
