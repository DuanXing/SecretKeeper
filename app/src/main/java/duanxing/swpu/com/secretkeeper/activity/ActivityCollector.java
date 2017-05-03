package duanxing.swpu.com.secretkeeper.activity;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Created by DZP on 2017/5/3.
 */

public class ActivityCollector {
    /**存储所有的activity*/
    public static LinkedList<Activity> activities = new LinkedList<Activity>();

    /**
     * 添加当前Activity到管理当中
     * @param activity 新创建的Activity
     */
    public static void addActivity(Activity activity)
    {
        activities.add(activity);
    }

    /**
     * 将当前Activity被销毁，从管理当中移除
     * @param activity 被销毁的Activity
     */
    public static void removeActivity(Activity activity)
    {
        activities.remove(activity);
    }

    /**
     * 销毁所有的Activity
     */
    public static void finishAll()
    {
        for(Activity activity:activities)
        {
            if(!activity.isFinishing())
            {
                activity.finish();
            }
        }
    }
}
