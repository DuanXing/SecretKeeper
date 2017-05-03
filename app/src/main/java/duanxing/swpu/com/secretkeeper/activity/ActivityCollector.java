package duanxing.swpu.com.secretkeeper.activity;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Created by duanxing on 2017/5/3.
 */

public class ActivityCollector {
    /**save all the activities*/
    public static LinkedList<Activity> activities = new LinkedList<Activity>();

    /**
     * add activity
     * @param activity the new activity
     */
    public static void addActivity(Activity activity)
    {
        activities.add(activity);
    }

    /**
     * remove activity
     * @param activity the Activity to remove
     */
    public static void removeActivity(Activity activity)
    {
        activities.remove(activity);
    }

    /**
     * destroy all activities
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
