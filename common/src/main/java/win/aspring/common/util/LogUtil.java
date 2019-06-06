package win.aspring.common.util;

import android.util.Log;

import win.aspring.common.BuildConfig;

/**
 * 日志打印类
 *
 * @author ASpring
 * @version 1.0
 * @since 2016-04-15 14:28
 */
public class LogUtil {
    private static final String LOG_TAG = "LOGUTIL";
    private static boolean DEBUG = BuildConfig.DEBUG;

    public LogUtil() {
    }

    public static final void analytics(String log) {
        if (DEBUG) {
            Log.d(LOG_TAG, log);
        }
    }

    public static final void error(String log) {
        if (DEBUG) {
            Log.e(LOG_TAG, "" + log);
        }
    }

    public static final void log(String log) {
        if (DEBUG) {
            Log.i(LOG_TAG, log);
        }
    }

    public static final void log(String tag, String log) {
        if (DEBUG) {
            Log.i(tag, log);
        }
    }

    public static final void logv(String log) {
        if (DEBUG) {
            Log.v(LOG_TAG, log);
        }
    }

    public static final void warn(String log) {
        if (DEBUG) {
            Log.w(LOG_TAG, log);
        }
    }
}
