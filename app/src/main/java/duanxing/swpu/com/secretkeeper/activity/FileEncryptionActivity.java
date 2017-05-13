package duanxing.swpu.com.secretkeeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.AesEncryption;
import duanxing.swpu.com.secretkeeper.utils.BaseEncryption;
import duanxing.swpu.com.secretkeeper.utils.CipherHandler;
import duanxing.swpu.com.secretkeeper.utils.DesEncryption;
import duanxing.swpu.com.secretkeeper.utils.DesedeEncryption;
import duanxing.swpu.com.secretkeeper.utils.FileUtil;
import duanxing.swpu.com.secretkeeper.utils.SM4Encryption;

public class FileEncryptionActivity extends BaseActivity {
    public enum ENCRYPTION_METHOD {
        AES_ENCRYPT,
        DES_ENCRYPT,
        DESEDE_ENCRYPT,
        SM4_ENCRYPT,
        NONE
    }

    // default encryption
    private ENCRYPTION_METHOD encrypt_method = ENCRYPTION_METHOD.AES_ENCRYPT;

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "FileEncryptionActivity";

    private Button btn_select_file;
    private EditText etTxt_file_path;
    private RadioGroup btnGp_encrypt_method;
    private RadioButton btnDefault_checked;
    private Button btn_encrypt;
    private EditText txtView_out_name;
    private ProgressBar progressBar;

    private String selectFilePath;
    private String savePath;

    private CipherHandler mHandler = new CipherHandler(this);
    private Message mMsg = new Message();
//    // deal the message of thread.
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message message) {
//            switch (message.what) {
//                case MSG_ENCRYPT_FAILED:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.encryptFailed), Toast.LENGTH_LONG).show();
//                    break;
//                case MSG_ENCRYPT_SUCCESS:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.encryptSuccess), Toast.LENGTH_LONG).show();
//                    break;
//                case MSG_ENCRYPT_INIT_FAILED:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.encryptInitFailed), Toast.LENGTH_LONG).show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_file_encryption);
    }

    @Override
    protected void initSubView() {
        txtView_out_name = (EditText) findViewById(R.id.outFileName);
        btn_select_file = (Button) findViewById(R.id.select_file);
        etTxt_file_path = (EditText) findViewById(R.id.file_path);
        btnGp_encrypt_method = (RadioGroup) findViewById(R.id.encrypt_method);
        btn_encrypt = (Button) findViewById(R.id.start_encrypt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnDefault_checked = (RadioButton) findViewById(R.id.aesRadioBtn);
        btnDefault_checked.setChecked(true);
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
                RadioButton radBtn = (RadioButton) findViewById(i);
                String encryptM = radBtn.getText().toString();
                if("AES".equals(encryptM)) {
                    encrypt_method = ENCRYPTION_METHOD.AES_ENCRYPT;
                }
                else if("DES".equals(encryptM)) {
                    encrypt_method = ENCRYPTION_METHOD.DES_ENCRYPT;
                }
                else if("DESede".equals(encryptM)) {
                    encrypt_method = ENCRYPTION_METHOD.DESEDE_ENCRYPT;
                }
                else if("SM4".equals(encryptM)){
                    encrypt_method = ENCRYPTION_METHOD.SM4_ENCRYPT;
                }
                else {
                    encrypt_method = ENCRYPTION_METHOD.NONE;
                }
            }
        });

        // the listener to btn_encrypt.
        btn_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the source file
                selectFilePath = etTxt_file_path.getText().toString();
                if("".equals(selectFilePath)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInput), Toast.LENGTH_LONG).show();
                    return;
                }

                // get the savePath.
                int index = selectFilePath.lastIndexOf('/');
                savePath = selectFilePath.substring(0, index+1);
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
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.fileExists), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.begin) + fileName, Toast.LENGTH_LONG).show();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // make the encryption
                        BaseEncryption encryption = null;
                        switch (encrypt_method) {
                            case AES_ENCRYPT:
                                encryption = new AesEncryption(selectFilePath, savePath, BaseEncryption.OP_CIPHER_MODE.BASE_ENCRYPT_MODE);
                                break;
                            case DES_ENCRYPT:
                                encryption = new DesEncryption(selectFilePath, savePath, BaseEncryption.OP_CIPHER_MODE.BASE_ENCRYPT_MODE);
                                break;
                            case DESEDE_ENCRYPT:
                                encryption = new DesedeEncryption(selectFilePath, savePath, BaseEncryption.OP_CIPHER_MODE.BASE_ENCRYPT_MODE);
                                break;
                            case SM4_ENCRYPT:
                                encryption = new SM4Encryption(selectFilePath, savePath, BaseEncryption.OP_CIPHER_MODE.BASE_ENCRYPT_MODE);
                                break;
                            case NONE:
                                break;
                            default:
                                break;
                        }

                        // encrypt
                        if(null != encryption) {
                            if(!encryption.init()) {
                                Log.e(TAG, getResources().getString(R.string.encryptInitFailed));
                                mMsg.what = CipherHandler.MSG_ENCRYPT_INIT_FAILED;
                                mHandler.sendMessage(mMsg);

                                return;
                            }

                            if(!encryption.doFinal()) {
                                Log.e(TAG, getResources().getString(R.string.encryptFailed));
                                mMsg.what = CipherHandler.MSG_ENCRYPT_FAILED;
                                mHandler.sendMessage(mMsg);

                                return;
                            }
                            else {
                                mMsg.what = CipherHandler.MSG_ENCRYPT_SUCCESS;
                                mHandler.sendMessage(mMsg);
                            }
                        }
                        else {
                            mMsg.what = CipherHandler.MSG_ENCRYPT_FAILED;
                            mHandler.sendMessage(mMsg);
                        }
                    }
                });
                thread.start();
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
            String realPath = FileUtil.getRealPath(uri, FileEncryptionActivity.this);
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
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.chooseFile)), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, getResources().getString(R.string.noFileManager), Toast.LENGTH_SHORT).show();
        }
    }
}
