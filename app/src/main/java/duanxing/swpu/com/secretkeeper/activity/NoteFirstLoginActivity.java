package duanxing.swpu.com.secretkeeper.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.crypto.Cipher;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.DatabaseCipher;
import duanxing.swpu.com.secretkeeper.utils.DatabaseHelper;

public class NoteFirstLoginActivity extends BaseActivity {
    private Button btn_register;
    private EditText ed_q1;
    private EditText ed_q2;
    private EditText ed_q3;
    private EditText ed_a1;
    private EditText ed_a2;
    private EditText ed_a3;
    private EditText ed_pd1;
    private EditText ed_pd2;

    private static final String TAG = "NoteFirstLoginActivity";

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_note_first_login);
    }

    @Override
    protected void initSubView() {
        btn_register = (Button) findViewById(R.id.btn_register);
        ed_q1 = (EditText) findViewById(R.id.ed_question1);
        ed_q2 = (EditText) findViewById(R.id.ed_question2);
        ed_q3 = (EditText) findViewById(R.id.ed_question3);
        ed_a1 = (EditText) findViewById(R.id.ed_answer1);
        ed_a2 = (EditText) findViewById(R.id.ed_answer2);
        ed_a3 = (EditText) findViewById(R.id.ed_answer3);
        ed_pd1 = (EditText) findViewById(R.id.ed_inputPass);
        ed_pd2 = (EditText) findViewById(R.id.ed_inputPass2);
    }

    @Override
    protected void setListener() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strQ1 = ed_q1.getText().toString();
                if("".equals(strQ1)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.QAinvalid), Toast.LENGTH_LONG).show();
                    return;
                }
                String strQ2 = ed_q2.getText().toString();
                if("".equals(strQ2)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.QAinvalid), Toast.LENGTH_LONG).show();
                    return;
                }
                String strQ3 = ed_q3.getText().toString();
                if("".equals(strQ3)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.QAinvalid), Toast.LENGTH_LONG).show();
                    return;
                }

                String strA1 = ed_a1.getText().toString();
                if("".equals(strA1)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.QAinvalid), Toast.LENGTH_LONG).show();
                    return;
                }
                String strA2 = ed_a2.getText().toString();
                if("".equals(strA2)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.QAinvalid), Toast.LENGTH_LONG).show();
                    return;
                }
                String strA3 = ed_a3.getText().toString();
                if("".equals(strA3)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.QAinvalid), Toast.LENGTH_LONG).show();
                    return;
                }

                String strPd1 = ed_pd1.getText().toString();
                if("".equals(strPd1)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noPD1), Toast.LENGTH_LONG).show();
                    return;
                }
                String strPd2 = ed_pd2.getText().toString();
                if("".equals(strPd2)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noPD2), Toast.LENGTH_LONG).show();
                    return;
                }
                if(!strPd1.equals(strPd2)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.PDinvalid), Toast.LENGTH_LONG).show();
                    return;
                }
                if(strPd1.length() < 8) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.pdLenError), Toast.LENGTH_LONG).show();
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(NoteFirstLoginActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                Log.i(TAG, "db file path: " + db.getPath());

                // insert user info
                DatabaseCipher databaseCipher = new DatabaseCipher(Cipher.ENCRYPT_MODE);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.USER_KEY_NAME, databaseCipher.doFinal(DatabaseHelper.USER_VALUE_NAME));
                contentValues.put(DatabaseHelper.USER_KEY_Q1, databaseCipher.doFinal(strQ1));
                contentValues.put(DatabaseHelper.USER_KEY_Q2, databaseCipher.doFinal(strQ2));
                contentValues.put(DatabaseHelper.USER_KEY_Q3, databaseCipher.doFinal(strQ3));
                contentValues.put(DatabaseHelper.USER_KEY_A1, databaseCipher.doFinal(strA1));
                contentValues.put(DatabaseHelper.USER_KEY_A2, databaseCipher.doFinal(strA2));
                contentValues.put(DatabaseHelper.USER_KEY_A3, databaseCipher.doFinal(strA3));
                contentValues.put(DatabaseHelper.USER_KEY_PD, databaseCipher.doFinal(strPd1));
                db.insert(DatabaseHelper.TB_USER, null, contentValues);

                db.close();

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registerSuccess), Toast.LENGTH_LONG).show();

                // finish this activity
                finish();

                // turn to note list
                enterActivity(NoteListActivity.class);
            }
        });
    }

    private void enterActivity(Class<?> cls) {
        Intent intent = new Intent(NoteFirstLoginActivity.this, cls);
        startActivity(intent);
    }

    @Override
    protected void processLogic() {

    }
}
