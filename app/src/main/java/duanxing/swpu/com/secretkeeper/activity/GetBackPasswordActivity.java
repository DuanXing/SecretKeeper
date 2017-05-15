package duanxing.swpu.com.secretkeeper.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.Cipher;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.DatabaseCipher;
import duanxing.swpu.com.secretkeeper.utils.DatabaseHelper;

public class GetBackPasswordActivity extends BaseActivity {
    private TextView txt_q1;
    private TextView txt_q2;
    private TextView txt_q3;
    private EditText ed_a1;
    private EditText ed_a2;
    private EditText ed_a3;
    private TextView txt_hint;
    private EditText ed_pd1;
    private EditText ed_pd2;
    private Button btn_ensure;

    private String answer1;
    private String answer2;
    private String answer3;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_get_back_password);
    }

    @Override
    protected void initSubView() {
        txt_q1 = (TextView) findViewById(R.id.txt_q1);
        txt_q2 = (TextView) findViewById(R.id.txt_q2);
        txt_q3 = (TextView) findViewById(R.id.txt_q3);
        ed_a1 = (EditText) findViewById(R.id.ed_answer1);
        ed_a2 = (EditText) findViewById(R.id.ed_answer2);
        ed_a3 = (EditText) findViewById(R.id.ed_answer3);
        txt_hint = (TextView) findViewById(R.id.txt_hint);
        btn_ensure = (Button) findViewById(R.id.ensure);
        ed_pd1 = (EditText) findViewById(R.id.ed_pd1);
        ed_pd2 = (EditText) findViewById(R.id.ed_pd2);

        initQuestion();
    }

    @Override
    protected void setListener() {
        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断问题是否回答正确
                String str = ed_a1.getText().toString();
                if(!str.equals(answer1)) {
                    txt_hint.setText(getResources().getString(R.string.answerError));
                    return;
                }
                str = ed_a2.getText().toString();
                if(!str.equals(answer2)) {
                    txt_hint.setText(getResources().getString(R.string.answerError));
                    return;
                }
                str = ed_a3.getText().toString();
                if(!str.equals(answer3)) {
                    txt_hint.setText(getResources().getString(R.string.answerError));
                    return;
                }

                // 判断密码是否合法
                String strPd1 = ed_pd1.getText().toString();
                if("".equals(strPd1)) {
                    txt_hint.setText(getResources().getString(R.string.noPD1));
                    return;
                }
                String strPd2 = ed_pd2.getText().toString();
                if("".equals(strPd2)) {
                    txt_hint.setText(getResources().getString(R.string.noPD2));
                    return;
                }
                if(!strPd1.equals(strPd2)) {
                    txt_hint.setText(getResources().getString(R.string.PDinvalid));
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(GetBackPasswordActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                DatabaseCipher databaseCipher = new DatabaseCipher(Cipher.ENCRYPT_MODE);

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.USER_KEY_PD, databaseCipher.doFinal(strPd1));

                // update value in database
                db.update(DatabaseHelper.TB_USER, contentValues, DatabaseHelper.USER_KEY_NAME + "=?", new String[]{DatabaseHelper.USER_VALUE_NAME});

                db.close();

                finish();

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.getPdSuccess), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void processLogic() {

    }

    /**
     * initialize questions
     */
    private void initQuestion() {
        DatabaseHelper databaseHelper = new DatabaseHelper(GetBackPasswordActivity.this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        DatabaseCipher databaseCipher = new DatabaseCipher(Cipher.DECRYPT_MODE);

        Cursor cursor = db.query(DatabaseHelper.TB_USER, new String[] {DatabaseHelper.USER_KEY_Q1, DatabaseHelper.USER_KEY_Q2, DatabaseHelper.USER_KEY_Q3,
                    DatabaseHelper.USER_KEY_A1, DatabaseHelper.USER_KEY_A2, DatabaseHelper.USER_KEY_A3}, "id=?", new String[] {"1"}, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            String str = cursor.getString(0);
            txt_q1.setText(databaseCipher.doFinal(str));
            str = cursor.getString(1);
            txt_q2.setText(databaseCipher.doFinal(str));
            str = cursor.getString(2);
            txt_q3.setText(databaseCipher.doFinal(str));
            str = cursor.getString(3);
            answer1 = databaseCipher.doFinal(str);
            str = cursor.getString(4);
            answer2 = databaseCipher.doFinal(str);
            str = cursor.getString(5);
            answer3 = databaseCipher.doFinal(str);
        }
        else {
            txt_hint.setText(getResources().getString(R.string.dbError));
        }

        db.close();
    }
}
