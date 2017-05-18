package duanxing.swpu.com.secretkeeper.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.utils.DatabaseHelper;
import duanxing.swpu.com.secretkeeper.utils.EmailSender;

public class VerifyCodeDialogActivity extends BaseActivity {
    private EditText ed_verifyCode;
    private Button btn_sendMail;
    private Button btn_ensure;
    private TimeCount timeCount;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_verify_code_dialog);
    }

    @Override
    protected void initSubView() {
        ed_verifyCode = (EditText) findViewById(R.id.ed_verifyCode);
        btn_sendMail = (Button) findViewById(R.id.btn_sendCode);
        btn_ensure = (Button) findViewById(R.id.btn_ensure);

        timeCount = new TimeCount(60 * 1000, 1000);
    }

    @Override
    protected void setListener() {
        btn_sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EmailSender().execute();
                timeCount.start();
            }
        });

        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCode = ed_verifyCode.getText().toString();
                if("".equals(strCode)) {
                    Toast.makeText(VerifyCodeDialogActivity.this, getResources().getString(R.string.invalidCode), Toast.LENGTH_SHORT).show();
                    return;
                }
                int code = Integer.valueOf(strCode);

                if(code == EmailSender.verifyCode && 0 != EmailSender.verifyCode) {
                    DatabaseHelper.deleteDatabase();

                    Toast.makeText(VerifyCodeDialogActivity.this, "初始化成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(VerifyCodeDialogActivity.this, "验证码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void processLogic() {

    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_sendMail.setEnabled(false);
            btn_sendMail.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            btn_sendMail.setText("重新发送验证码");
            btn_sendMail.setEnabled(true);
        }
    }
}
