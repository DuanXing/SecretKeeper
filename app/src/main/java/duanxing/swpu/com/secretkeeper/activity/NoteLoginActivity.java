package duanxing.swpu.com.secretkeeper.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.DatabaseHelper;

public class NoteLoginActivity extends BaseActivity {
    private Button btn_login;
    private Button btn_forget;
    private Button btn_recover;
    private EditText ed_password;
    private TextView txt_hint;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

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

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
