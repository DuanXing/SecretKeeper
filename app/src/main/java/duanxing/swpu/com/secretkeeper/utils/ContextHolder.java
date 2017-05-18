package duanxing.swpu.com.secretkeeper.utils;

import android.content.Context;

/**
 * Created by duanxing on 18/05/2017.
 */

/**
 * 获取Application Context，以便在自定义类里使用context
 */
public class ContextHolder {
    static Context appContext = null;

    public static void init(Context context) {
        if(null == appContext)
            appContext = context;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
