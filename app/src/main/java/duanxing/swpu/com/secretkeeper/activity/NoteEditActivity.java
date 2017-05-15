package duanxing.swpu.com.secretkeeper.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import duanxing.swpu.com.secretkeeper.R;

public class NoteEditActivity extends BaseActivity {
    private EditText ed_title;
    private EditText ed_content;
    private Button btn_save;

    private int id;
    private boolean isNew;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_note_edit);
    }

    @Override
    protected void initSubView() {
        btn_save = (Button) findViewById(R.id.btn_save);
        ed_title = (EditText) findViewById(R.id.ed_title);
        ed_content = (EditText) findViewById(R.id.ed_content);
    }

    @Override
    protected void setListener() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTitle = ed_title.getText().toString();
                if("".equals(strTitle)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noTitle), Toast.LENGTH_LONG).show();
                    return;
                }
                String strContent = ed_content.getText().toString();
                if("".equals(strContent)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noContent), Toast.LENGTH_LONG).show();
                    return;
                }

                // TODO update to database

                finish();
            }
        });
    }

    @Override
    protected void processLogic() {

    }
}
