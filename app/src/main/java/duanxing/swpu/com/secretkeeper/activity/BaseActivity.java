package duanxing.swpu.com.secretkeeper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by DZP on 2017/5/3.
 */

public abstract class BaseActivity extends Activity  {

    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        ActivityCollector.addActivity(this);
        initView();
    }

    /**
     * 初始化所有View
     */
    private void initView() {
        loadViewLayout();
        findViewById();
        setListener();
    }


    /**
     *初始化该Activity对应的活动
     */
    protected abstract void loadViewLayout();

    /**
     * 初始化Activity当中的控件
     */
    protected abstract void findViewById();

    /**
     * 为控件添加监听事件
     */
    protected abstract void setListener();

    /**
     * 后台读取数据操作
     */
    protected abstract void processLogic();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
