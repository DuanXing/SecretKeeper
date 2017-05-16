package duanxing.swpu.com.secretkeeper.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.crypto.Cipher;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.DatabaseCipher;
import duanxing.swpu.com.secretkeeper.utils.DatabaseHelper;

public class NoteEditActivity extends BaseActivity {
    private EditText ed_title;
    private EditText ed_content;
    private Button btn_save;

    private int id = -1;
    private boolean isNew = true;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_note_edit);
    }

    @Override
    protected void initSubView() {
        btn_save = (Button) findViewById(R.id.btn_save);
        ed_title = (EditText) findViewById(R.id.ed_title);
        ed_content = (EditText) findViewById(R.id.ed_content);

        Intent intent = getIntent();
        isNew = intent.getBooleanExtra("isNew", true);
        if(!isNew) {
            id = intent.getIntExtra("id", -1);
            ed_title.setText(intent.getStringExtra("title"));
            ed_content.setText(intent.getStringExtra("content"));

            if(-1 == id) {
                isNew = true;
            }
        }
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

                // encrypt string
                DatabaseCipher cipher = new DatabaseCipher(Cipher.ENCRYPT_MODE);
                strTitle = cipher.doFinal(strTitle);
                strContent = cipher.doFinal(strContent);

                DatabaseHelper helper = new DatabaseHelper(NoteEditActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.NOTE_KEY_TITLE, strTitle);
                contentValues.put(DatabaseHelper.NOTE_KEY_CONTENT, strContent);

                if(isNew) {
                    db.insert(DatabaseHelper.TB_NOTE, null, contentValues);
                }
                else {
                    db.update(DatabaseHelper.TB_NOTE, contentValues, DatabaseHelper.NOTE_KEY_ID + "=?", new String[]{String.valueOf(id)});
                }

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();

                db.close();

                setResult(NoteListActivity.NOTE_EDIT_RESULT);
                finish();
            }
        });
    }

    @Override
    protected void processLogic() {

    }
}
