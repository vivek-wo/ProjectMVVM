package com.vivek.wo.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

public final class BarUtils {

    /**
     * 设置状态栏颜色
     * <p>
     * 非沉浸式设置，只设置状态栏颜色，布局非全屏模式，无需在layout中预留状态栏高度
     *
     * @param activity
     * @param color
     */
    public static void setColor(@NonNull Activity activity, @ColorInt int color) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        Window window = activityWeakReference.get().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    /**
     * 设置透明状态栏
     * <p>
     * 沉浸式设置，布局全屏模式，建议layout中通过{@link View#setFitsSystemWindows(boolean)} 预留状态栏高度
     *
     * @param activity
     */
    public static void setTransparent(@NonNull Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        Window window = activityWeakReference.get().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 设置状态栏暗色模式，支持Android M以上
     *
     * @param activity 目标activity
     */
    public static void setDarkMode(@NonNull Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        darkMode(activityWeakReference.get().getWindow(), true);
    }

    /**
     * 设置状态栏亮色，支持Android M以上
     *
     * @param activity 目标activity
     */
    public static void setLightMode(@NonNull Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        darkMode(activityWeakReference.get().getWindow(), false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void darkMode(Window window, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            if (dark) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context 目标Context·
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}