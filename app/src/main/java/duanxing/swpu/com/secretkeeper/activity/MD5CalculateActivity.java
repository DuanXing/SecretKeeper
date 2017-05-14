package duanxing.swpu.com.secretkeeper.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.FileUtil;
import duanxing.swpu.com.secretkeeper.utils.Md5Calculate;

public class MD5CalculateActivity extends BaseActivity {
    private Button btn_chooseFile;
    private EditText ed_filePath;
    private Button btn_calculate;
    private ProgressBar progressBar;
    private TextView txt_md5Value;

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "MD5CalculateActivity";

    // message what
    private static final int MSG_MD5_BUSY = 0;
    private static final int MSG_MD5_FREE = 1;
    private static final int MSG_MD5_VALUE = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_MD5_BUSY:
                    Md5Calculate.isBusy = true;
                    progressBar.setAlpha(1);
                    btn_calculate.setEnabled(false);
                    btn_calculate.setText(getResources().getString(R.string.calculating));
                    break;
                case MSG_MD5_FREE:
                    Md5Calculate.isBusy = false;
                    progressBar.setAlpha(0);
                    btn_calculate.setEnabled(true);
                    btn_calculate.setText(getResources().getString(R.string.calculate));
                    break;
                case MSG_MD5_VALUE:
                    txt_md5Value.setText((String)message.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_md5_calculate);
    }

    @Override
    protected void initSubView() {
        btn_chooseFile = (Button) findViewById(R.id.select_file);
        btn_calculate = (Button) findViewById(R.id.calculate);
        ed_filePath = (EditText) findViewById(R.id.file_path);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt_md5Value = (TextView) findViewById(R.id.md5Value);
        progressBar.setAlpha(0);

        if(Md5Calculate.isBusy) {
            progressBar.setAlpha(1);
            btn_calculate.setEnabled(false);
            btn_calculate.setText(getResources().getString(R.string.calculating));
        }
    }

    @Override
    protected void setListener() {
        txt_md5Value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(txt_md5Value.getText());
                Toast.makeText(getApplicationContext(), "复制成功", Toast.LENGTH_LONG).show();
            }
        });

        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message mMsg = Message.obtain();
                        mMsg.what = MSG_MD5_BUSY;
                        mHandler.sendMessage(mMsg);

                        String md5Value = Md5Calculate.getMd5OfFile(ed_filePath.getText().toString());
                        mMsg = Message.obtain();
                        mMsg.obj = md5Value;
                        mMsg.what = MSG_MD5_VALUE;
                        mHandler.sendMessage(mMsg);

                        mMsg = Message.obtain();
                        mMsg.what = MSG_MD5_FREE;
                        mHandler.sendMessage(mMsg);
                    }
                });
                thread.start();
            }
        });

        btn_chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
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
            String realPath = FileUtil.getRealPath(uri, MD5CalculateActivity.this);
            if (uri.getPath() != null) {
                ed_filePath.setText(realPath);
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

    @Override
    public void onBackPressed() {
        if(!Md5Calculate.isBusy) {
            super.onBackPressed();
        }
    }
}
