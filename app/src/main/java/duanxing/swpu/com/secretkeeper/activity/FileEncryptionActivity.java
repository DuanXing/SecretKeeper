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

    private Button select_file; // 选择文件按钮
    private EditText file_path; // 文件路径展示控件
    private RadioGroup encrypt_method; // 加密方法
    private Button start_encrypt; // 开始加密按钮

    private String selectFilePath;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_file_encryption);
    }

    @Override
    protected void findViewById() {
        select_file = (Button) findViewById(R.id.select_file);
        file_path = (EditText) findViewById(R.id.file_path);
        encrypt_method = (RadioGroup) findViewById(R.id.encrypt_method);
        start_encrypt = (Button) findViewById(R.id.start_encrypt);
    }

    @Override
    protected void setListener() {

        // 选择文件监听
        select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        // 选择加密方法监听
        encrypt_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

        // 开始加密监听
        start_encrypt.setOnClickListener(new View.OnClickListener() {
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
                file_path.setText(uri.getPath().toString());

            }
            Log.i(TAG, "------->" + uri.getPath());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 弹出选择文件窗口
     */
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
        }
    }
}
