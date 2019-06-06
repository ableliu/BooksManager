package win.aspring.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import win.aspring.common.R;
import win.aspring.common.util.OutdatedUtils;

/**
 * Toast
 */
public class CustomToast {
    @ColorInt
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");

    @ColorInt
    private static final int ERROR_COLOR = Color.parseColor("#D8524E");
    @ColorInt
    private static final int INFO_COLOR = Color.parseColor("#3278B5");
    @ColorInt
    private static final int SUCCESS_COLOR = Color.parseColor("#5BB75B");
    @ColorInt
    private static final int WARNING_COLOR = Color.parseColor("#FB9B4D");
    @ColorInt
    private static final int NORMAL_COLOR = Color.parseColor("#444344");

    private static final String TOAST_TYPEFACE = "sans-serif-condensed";

    private CustomToast() {
    }

    public static Toast normal(Context context, @NonNull String message) {
        return normal(context, message, Toast.LENGTH_SHORT, null);
    }

    public static Toast normal(Context context, @NonNull String message, Drawable icon) {
        return normal(context, message, Toast.LENGTH_SHORT, icon);
    }

    public static Toast normal(Context context, @NonNull String message, int duration) {
        return normal(context, message, duration, null);
    }

    public static Toast normal(Context context, @NonNull String message, int duration, Drawable icon) {
        return custom(context, message, icon, NORMAL_COLOR, duration);
    }

    public static Toast warning(Context context, @NonNull String message) {
        return warning(context, message, Toast.LENGTH_SHORT, true);
    }

    public static Toast warning(Context context, @NonNull String message, int duration) {
        return warning(context, message, duration, true);
    }

    public static Toast warning(Context context, @NonNull String message, int duration, boolean withIcon) {
        Drawable icon = null;
        if (withIcon) {
            icon = OutdatedUtils.getDrawable(context, R.drawable.toast_warning);
        }
        return custom(context, message, icon, WARNING_COLOR, duration);
    }

    public static Toast info(Context context, @NonNull String message) {
        return info(context, message, Toast.LENGTH_SHORT, true);
    }

    public static Toast info(Context context, @NonNull String message, int duration) {
        return info(context, message, duration, true);
    }

    public static Toast info(Context context, @NonNull String message, int duration, boolean withIcon) {
        Drawable icon = null;
        if (withIcon) {
            icon = OutdatedUtils.getDrawable(context, R.drawable.toast_info);
        }
        return custom(context, message, icon, INFO_COLOR, duration);
    }

    public static Toast success(Context context, @NonNull String message) {
        return success(context, message, Toast.LENGTH_SHORT, true);
    }

    public static Toast success(Context context, @NonNull String message, int duration) {
        return success(context, message, duration, true);
    }

    public static Toast success(Context context, @NonNull String message, int duration, boolean withIcon) {
        Drawable icon = null;
        if (withIcon) {
            icon = OutdatedUtils.getDrawable(context, R.drawable.toast_success);
        }
        return custom(context, message, icon, SUCCESS_COLOR, duration);
    }

    public static Toast error(Context context, @NonNull String message) {
        return error(context, message, Toast.LENGTH_SHORT, true);
    }

    public static Toast error(Context context, @NonNull String message, int duration) {
        return error(context, message, duration, true);
    }

    public static Toast error(Context context, @NonNull String message, int duration, boolean withIcon) {
        Drawable icon = null;
        if (withIcon) {
            icon = OutdatedUtils.getDrawable(context, R.drawable.toast_error);
        }
        return custom(context, message, icon, ERROR_COLOR, duration);
    }

    public static Toast custom(Context context, @NonNull String message, @ColorInt int tintColor) {
        return custom(context, message, null, DEFAULT_TEXT_COLOR, tintColor, Toast.LENGTH_SHORT);
    }

    public static Toast custom(Context context, @NonNull String message, Drawable icon, @ColorInt int tintColor) {
        return custom(context, message, icon, DEFAULT_TEXT_COLOR, tintColor, Toast.LENGTH_SHORT);
    }

    public static Toast custom(Context context, @NonNull String message, @ColorInt int tintColor, int duration) {
        return custom(context, message, null, DEFAULT_TEXT_COLOR, tintColor, duration);
    }

    public static Toast custom(Context context, @NonNull String message, Drawable icon, @ColorInt int tintColor, int duration) {
        return custom(context, message, icon, DEFAULT_TEXT_COLOR, tintColor, duration);
    }

    public static Toast custom(Context context, @NonNull String message, @DrawableRes int iconRes,
                               @ColorInt int textColor, @ColorInt int tintColor, int duration) {
        return custom(context, message, OutdatedUtils.getDrawable(context, iconRes), textColor, tintColor, duration);
    }

    /**
     * 自定义toast方法
     *
     * @param message   提示消息文本
     * @param icon      提示消息的icon,传入null代表不显示
     * @param textColor 提示消息文本颜色
     * @param tintColor 提示背景颜色
     * @param duration  显示时长
     * @return
     */
    public static Toast custom(Context context, @NonNull String message, Drawable icon,
                               @ColorInt int textColor, @ColorInt int tintColor, int duration) {
        Toast currentToast = new Toast(context);
        View toastLayout = LayoutInflater.from(context).inflate(R.layout.custom_toast_view, null);
        ImageView toastIcon = toastLayout.findViewById(R.id.toast_icon);
        TextView toastText = toastLayout.findViewById(R.id.toast_text);

        Drawable drawableFrame = OutdatedUtils.getDrawable(context, R.drawable.toast_frame);
        drawableFrame.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        OutdatedUtils.setBackground(toastLayout, drawableFrame);

        if (icon == null) {
            toastIcon.setVisibility(View.GONE);
        } else {
            OutdatedUtils.setBackground(toastIcon, icon);
        }

        toastText.setTextColor(textColor);
        toastText.setText(message);
        toastText.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL));

        currentToast.setView(toastLayout);
        currentToast.setDuration(duration);
        currentToast.show();
        return currentToast;
    }
}