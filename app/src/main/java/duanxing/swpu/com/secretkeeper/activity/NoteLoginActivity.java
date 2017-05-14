package duanxing.swpu.com.secretkeeper.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import duanxing.swpu.com.secretkeeper.R;

public class NoteLoginActivity extends BaseActivity {
    private Button btn_login;
    private Button btn_forget;
    private EditText ed_password;
    private TextView txt_hint;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_note_login);
    }

    @Override
    protected void initSubView() {
        btn_forget = (Button) findViewById(R.id.forget);
        btn_login = (Button) findViewById(R.id.login);
        ed_password = (EditText) findViewById(R.id.password);
        txt_hint = (TextView) findViewById(R.id.loginHint);
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

        ed_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void processLogic() {

    }
}
