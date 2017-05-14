package duanxing.swpu.com.secretkeeper.activity;

import android.view.View;
import android.widget.Button;

import duanxing.swpu.com.secretkeeper.R;

public class NoteEditActivity extends BaseActivity {
    private Button btn_save;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_note_edit);
    }

    @Override
    protected void initSubView() {
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    @Override
    protected void setListener() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void processLogic() {

    }
}
