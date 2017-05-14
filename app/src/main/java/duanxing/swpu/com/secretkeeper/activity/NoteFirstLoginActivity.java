package duanxing.swpu.com.secretkeeper.activity;

import android.view.View;
import android.widget.Button;

import duanxing.swpu.com.secretkeeper.R;

public class NoteFirstLoginActivity extends BaseActivity {
    private Button btn_register;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_note_first_login);
    }

    @Override
    protected void initSubView() {
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void setListener() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void processLogic() {

    }
}
