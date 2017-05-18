package duanxing.swpu.com.secretkeeper.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import duanxing.swpu.com.secretkeeper.utils.EmailSender;

public class NoteLoginActivity extends BaseActivity {
    private Button btn_login;
    private Button btn_forget;
    private Button btn_recover;
    private EditText ed_password;
    private TextView txt_hint;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

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
                        finish();
                        enterActivity(NoteListActivity.class);
                    }
                    else {
                        txt_hint.setText(getResources().getString(R.string.error));
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
                builder = new AlertDialog.Builder(NoteLoginActivity.this);
                alertDialog = builder.setMessage("这是一个危险的操作，将丢失所有的已记录信息！")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper.deleteDatabase();

                                Toast.makeText(NoteLoginActivity.this, "初始化成功！", Toast.LENGTH_SHORT).show();

                                // finish this activity
                                finish();
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }

    private void enterActivity(Class<?> cls) {
        Intent intent = new Intent(NoteLoginActivity.this, cls);
        startActivity(intent);
    }

    @Override
    protected void processLogic() {

    }
}
