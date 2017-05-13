package duanxing.swpu.com.secretkeeper.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import duanxing.swpu.com.secretkeeper.R;

/**
 * Created by duanxing on 13/05/2017.
 */

public class CipherHandler extends Handler{
    // message what value
    public static final int MSG_ENCRYPT_FAILED = 0;
    public static final int MSG_ENCRYPT_SUCCESS = 1;
    public static final int MSG_ENCRYPT_INIT_FAILED = 2;
    public static final int MSG_DECRYPT_FAILED = 3;
    public static final int MSG_DECRYPT_SUCCESS = 4;
    public static final int MSG_DECRYPT_INIT_FAILED = 5;
    public static final int MSG_DECRYPT_INVALID_FILE = 6;

    private Context context;

    public CipherHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case MSG_ENCRYPT_FAILED:
                Toast.makeText(context, context.getResources().getString(R.string.encryptFailed), Toast.LENGTH_LONG).show();
                break;
            case MSG_ENCRYPT_SUCCESS:
                Toast.makeText(context, context.getResources().getString(R.string.encryptSuccess), Toast.LENGTH_LONG).show();
                break;
            case MSG_ENCRYPT_INIT_FAILED:
                Toast.makeText(context, context.getResources().getString(R.string.encryptInitFailed), Toast.LENGTH_LONG).show();
                break;
            case MSG_DECRYPT_FAILED:
                Toast.makeText(context, context.getResources().getString(R.string.decryptFailed), Toast.LENGTH_LONG).show();
                break;
            case MSG_DECRYPT_SUCCESS:
                Toast.makeText(context, context.getResources().getString(R.string.decryptSuccess), Toast.LENGTH_LONG).show();
                break;
            case MSG_DECRYPT_INIT_FAILED:
                Toast.makeText(context, context.getResources().getString(R.string.decryptInitFailed), Toast.LENGTH_LONG).show();
                break;
            case MSG_DECRYPT_INVALID_FILE:
                Toast.makeText(context, context.getResources().getString(R.string.invalidFile), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
