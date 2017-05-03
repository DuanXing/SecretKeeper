package duanxing.swpu.com.secretkeeper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by duanxing on 2017/5/3.
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
     * initialize all views
     */
    private void initView() {
        loadViewLayout();
        initSubView();
        setListener();
    }


    /**
     * initialize activity of the view
     */
    protected abstract void loadViewLayout();

    /**
     * initialize the sub view
     */
    protected abstract void initSubView();

    /**
     * initialize listener of the view
     */
    protected abstract void setListener();

    /**
     * background work
     */
    protected abstract void processLogic();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
