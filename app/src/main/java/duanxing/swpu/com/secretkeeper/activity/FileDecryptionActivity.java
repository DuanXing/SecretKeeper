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
import android.widget.TextView;
import android.widget.Toast;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.AesEncryption;
import duanxing.swpu.com.secretkeeper.utils.BaseEncryption;
import duanxing.swpu.com.secretkeeper.utils.CipherHandler;
import duanxing.swpu.com.secretkeeper.utils.DesEncryption;
import duanxing.swpu.com.secretkeeper.utils.DesedeEncryption;
import duanxing.swpu.com.secretkeeper.utils.EncryptionHelper;
import duanxing.swpu.com.secretkeeper.utils.FileUtil;
import duanxing.swpu.com.secretkeeper.utils.SM4Encryption;

public class FileDecryptionActivity extends BaseActivity {
    public enum ENCRYPTION_METHOD {
        AES_ENCRYPT,
        DES_ENCRYPT,
        DESEDE_ENCRYPT,
        SM4_ENCRYPT,
        NONE
    }

    private Button btn_select_file;
    private EditText etTxt_file_path;
    private Button btn_decrypt;
    private EditText etTxt_out_name;
    private TextView txtView_encryption_hint;
    private ProgressBar progressBar;

    // default encryption
    private ENCRYPTION_METHOD encrypt_method = ENCRYPTION_METHOD.AES_ENCRYPT;

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "FileDecryptionActivity";

    private String selectFilePath;
    private String savePath;

    private CipherHandler mHandler = new CipherHandler(this);
    private Message mMsg = new Message();
//    // deal the message of thread.
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message message) {
//            switch (message.what) {
//                case MSG_DECRYPT_FAILED:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.decryptFailed), Toast.LENGTH_LONG).show();
//                    break;
//                case MSG_DECRYPT_SUCCESS:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.decryptSuccess), Toast.LENGTH_LONG).show();
//                    break;
//                case MSG_DECRYPT_INIT_FAILED:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.decryptInitFailed), Toast.LENGTH_LONG).show();
//                    break;
//                case MSG_DECRYPT_INVALID_FILE:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalidFile) + selectFilePath.substring(selectFilePath.lastIndexOf('/')+1), Toast.LENGTH_LONG).show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_file_decryption);
    }

    @Override
    protected void initSubView() {
        etTxt_out_name = (EditText) findViewById(R.id.outFileName);
        btn_select_file = (Button) findViewById(R.id.select_file);
        etTxt_file_path = (EditText) findViewById(R.id.file_path);
        btn_decrypt = (Button) findViewById(R.id.start_decrypt);
        txtView_encryption_hint = (TextView) findViewById(R.id.encryptHint);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInput), Toast.LENGTH_LONG).show();
                    return;
                }

                // get the savePath.
                int index = selectFilePath.lastIndexOf('/');
                savePath = selectFilePath.substring(0, index+1);
                String fileName = selectFilePath.substring(index+1);
                String outName = etTxt_out_name.getText().toString();
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
                        // make the decryption
                        BaseEncryption decryption = null;
                        switch (encrypt_method) {
                            case AES_ENCRYPT:
                                decryption = new AesEncryption(selectFilePath, savePath, BaseEncryption.OP_CIPHER_MODE.BASE_DECRYPT_MODE);
                                break;
                            case DES_ENCRYPT:
                                decryption = new DesEncryption(selectFilePath, savePath, BaseEncryption.OP_CIPHER_MODE.BASE_DECRYPT_MODE);
                                break;
                            case DESEDE_ENCRYPT:
                                decryption = new DesedeEncryption(selectFilePath, savePath, BaseEncryption.OP_CIPHER_MODE.BASE_DECRYPT_MODE);
                                break;
                            case SM4_ENCRYPT:
                                decryption = new SM4Encryption(selectFilePath, savePath, BaseEncryption.OP_CIPHER_MODE.BASE_DECRYPT_MODE);
                                break;
                            case NONE:
                                mMsg.what = CipherHandler.MSG_DECRYPT_INVALID_FILE;
                                mHandler.sendMessage(mMsg);
                                return;
                            default:
                                break;
                        }

                        // decrypt
                        if(null != decryption) {
                            if(!decryption.init()) {
                                Log.e(TAG, getResources().getString(R.string.decryptInitFailed));
                                Message message = new Message();
                                message.what = CipherHandler.MSG_DECRYPT_INIT_FAILED;
                                mHandler.sendMessage(message);

                                return;
                            }

                            if(!decryption.doFinal()) {
                                Log.e(TAG, getResources().getString(R.string.decryptFailed));
                                Message message = new Message();
                                message.what = CipherHandler.MSG_DECRYPT_FAILED;
                                mHandler.sendMessage(message);

                                return;
                            }
                            else {
                                Message message = new Message();
                                message.what = CipherHandler.MSG_DECRYPT_SUCCESS;
                                mHandler.sendMessage(message);
                            }
                        }
                        else {
                            Message message = new Message();
                            message.what = CipherHandler.MSG_DECRYPT_FAILED;
                            mHandler.sendMessage(message);
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
            String realPath = FileUtil.getRealPath(uri, FileDecryptionActivity.this);
            if (uri.getPath() != null) {
                etTxt_file_path.setText(realPath);
            }

            // judge fileHeader.
            String fileHeader = EncryptionHelper.getFileHeader(realPath);
            if(EncryptionHelper.AES_FILE_HEADER.equals(fileHeader)) {
                txtView_encryption_hint.setText(getResources().getString(R.string.encrypt_method_aes));
                encrypt_method = ENCRYPTION_METHOD.AES_ENCRYPT;
            }
            else if(EncryptionHelper.DES_FILE_HEADER.equals(fileHeader)) {
                txtView_encryption_hint.setText(getResources().getString(R.string.encrypt_method_des));
                encrypt_method = ENCRYPTION_METHOD.DES_ENCRYPT;
            }
            else if(EncryptionHelper.DESEDE_FILE_HEADER.equals(fileHeader)) {
                txtView_encryption_hint.setText(getResources().getString(R.string.encrypt_method_desede));
                encrypt_method = ENCRYPTION_METHOD.DESEDE_ENCRYPT;
            }
            else if(EncryptionHelper.SM4_FILE_HEADER.equals(fileHeader)) {
                txtView_encryption_hint.setText(getResources().getString(R.string.encrypt_method_sm4));
                encrypt_method = ENCRYPTION_METHOD.SM4_ENCRYPT;
            }
            else {
                txtView_encryption_hint.setText(getResources().getString(R.string.encrypt_method_none));
                encrypt_method = ENCRYPTION_METHOD.NONE;
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
