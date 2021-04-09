package com.vivek.wo.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class ScreenUtils {

    /**
     * 获取屏幕内容显示高度
     * <p>
     * 不包含导航栏高度，当Activity活动中动态显示隐藏导航栏时，数值不更新
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕内容显示宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param context 目标Context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resourceId > 0) {
            try {
                result = res.getDimensionPixelSize(resourceId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取导航栏高度
     * <p>
     * 只获取导航栏高度，不能判断导航栏是否存在
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        int result = 0;
        if (resourceId > 0) {
            try {
                result = res.getDimensionPixelSize(resourceId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 判断当前是否存在导航栏
     * <p>
     * 当Activity活动中执行了动态显示隐藏导航栏时，数值不更新，无法判断准确数值
     *
     * @param windowManager
     * @return
     */
    public static boolean isNavigationBarExisted(WindowManager windowManager) {
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(realDisplayMetrics);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return realDisplayMetrics.heightPixels - displayMetrics.heightPixels > 0;
    }

    /**
     * dp 转 px
     * <p>
     * 如dp屏幕适配方法，不要直接转换dp值，用{@link Resources#getDimensionPixelSize}方法替换
     *
     * @param context
     * @param value
     * @return
     */
    public static float dp2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * px 转 dp
     *
     * @param context
     * @param value
     * @return
     */
    public static float px2dp(Context context, float value) {
        return value / context.getResources().getDisplayMetrics().density;
    }
}
