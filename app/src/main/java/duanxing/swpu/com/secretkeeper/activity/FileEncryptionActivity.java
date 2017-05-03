package duanxing.swpu.com.secretkeeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import duanxing.swpu.com.secretkeeper.R;

public class FileEncryptionActivity extends BaseActivity {


    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "FileEncryptionActivity";

    private Button btn_select_file;
    private EditText etTxt_file_path;
    private RadioGroup btnGp_encrypt_method;
    private Button btn_encrypt;

    private String selectFilePath;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_file_encryption);
    }

    @Override
    protected void initSubView() {
        btn_select_file = (Button) findViewById(R.id.select_file);
        etTxt_file_path = (EditText) findViewById(R.id.file_path);
        btnGp_encrypt_method = (RadioGroup) findViewById(R.id.encrypt_method);
        btn_encrypt = (Button) findViewById(R.id.start_encrypt);
    }

    @Override
    protected void setListener() {

        // the listener to file choose.
        btn_select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        // the listener to encryption choose.
        btnGp_encrypt_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

        // the listener to btn_encrypt.
        btn_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    protected void processLogic() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        // TODO Auto-generated method stub
        if (resultCode != Activity.RESULT_OK) {
            Log.e(TAG, "onActivityResult() error, resultCode: " + resultCode);
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == FILE_SELECT_CODE) {
            Uri uri = data.getData();
            if (uri.getPath() != null) {
                etTxt_file_path.setText(uri.getPath().toString());

            }
            Log.i(TAG, "------->" + uri.getPath());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Pop file choose dialog.
     */
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Choose file"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Sorry, there no file manager-_-!!", Toast.LENGTH_SHORT).show();
        }
    }
}
