package win.aspring.common.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * 全局上下文
 *
 * @author Abel
 * @version 1.0
 * @since 2016-04-14 09:04
 */
public class BaseApplication extends Application {

    static Context mContext;
    static Resources mResources;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mResources = mContext.getResources();
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) mContext;
    }

    public static Resources resources() {
        return mResources;
    }

    public static String string(int id) {
        return mResources.getString(id);
    }

    public static String string(int id, Object... args) {
        return mResources.getString(id, args);
    }
}
