package duanxing.swpu.com.secretkeeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.AesEncryption;
import duanxing.swpu.com.secretkeeper.utils.BaseEncryption;
import duanxing.swpu.com.secretkeeper.utils.DesEncryption;
import duanxing.swpu.com.secretkeeper.utils.DesedeEncryption;
import duanxing.swpu.com.secretkeeper.utils.FileUtil;

public class FileDecryptionActivity extends BaseActivity {
    public enum ENCRYPTION_METHOD {
        AES_ENCRYPT,
        DES_ENCRYPT,
        DESEDE_ENCRYPT,
        SM4_ENCRYPT
    }

    private Button btn_select_file;
    private EditText etTxt_file_path;
    private Button btn_decrypt;
    private EditText txtView_out_name;

    // default encryption
    private ENCRYPTION_METHOD encrypt_method = ENCRYPTION_METHOD.AES_ENCRYPT;

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "FileDecryptionActivity";

    private String selectFilePath;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_file_decryption);
    }

    @Override
    protected void initSubView() {
        txtView_out_name = (EditText) findViewById(R.id.outFileName);
        btn_select_file = (Button) findViewById(R.id.select_file);
        etTxt_file_path = (EditText) findViewById(R.id.file_path);
        btn_decrypt = (Button) findViewById(R.id.start_decrypt);
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

        // the listener to btn_decrypt
        btn_decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the source file
                selectFilePath = etTxt_file_path.getText().toString();
                if("".equals(selectFilePath)) {
                    Toast.makeText(getApplicationContext(), "No input file.", Toast.LENGTH_LONG).show();
                    return;
                }

                // get the savePath.
                int index = selectFilePath.lastIndexOf('/');
                String savePath = selectFilePath.substring(0, index+1);
                String fileName = selectFilePath.substring(index+1);
                String outName = txtView_out_name.getText().toString();
                if(!"".equals(outName)) {
                    savePath += outName;
                } else {
                    savePath += FileUtil.getTimeStamp();
                    savePath += "_";
                    savePath += fileName;
                }

                // is the save path valid ?
                boolean pathInvalid = FileUtil.fileExists(savePath);
                if(pathInvalid) {
                    Toast.makeText(getApplicationContext(), "The out file is existed.", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Start to decrypt " + fileName, Toast.LENGTH_LONG).show();

                // make the decryption
                BaseEncryption decryption = null;
                decryption = new DesedeEncryption(BaseEncryption.OP_CIPHER_MODE.BASE_DECRYPT_MODE);

                // decrypt
                if(null != decryption) {
                    if(!decryption.init()) {
                        Log.e(TAG, "Decryption method init failed.");
                        Toast.makeText(getApplicationContext(), "Decryption method init failed.", Toast.LENGTH_LONG).show();

                        return;
                    }

                    if(!decryption.decrypt(selectFilePath, savePath)) {
                        Log.e(TAG, "Decrypt failed.");
                        Toast.makeText(getApplicationContext(), "Decrypt failed.", Toast.LENGTH_LONG).show();

                        return;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Decrypt success.", Toast.LENGTH_LONG).show();
                    }
                }
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

        // select file result.
        if (requestCode == FILE_SELECT_CODE) {
            Uri uri = data.getData();
            String realPath = FileUtil.getRealPath(uri, FileDecryptionActivity.this);
            if (uri.getPath() != null) {
                etTxt_file_path.setText(realPath);
            }
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
            Toast.makeText(this, "Sorry, no file manager-_-!!", Toast.LENGTH_SHORT).show();
        }
    }
}
