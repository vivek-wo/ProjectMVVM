package com.vivek.wo.common.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {

    /**
     * 显示软键盘，并尝试获取焦点
     *
     * @param view
     */
    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 隐藏软键盘，并尝试清除焦点
     *
     * @param view
     */
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 是否点击在EditText内部
     *
     * @param view
     * @param event
     * @return
     */
    public static boolean isClickInEditText(View view, MotionEvent event) {
        if (view instanceof EditText) {
            int[] location = {0, 0};
            view.getLocationInWindow(location);
            boolean inRect = event.getX() > location[0] && event.getX() < (location[0] + view.getWidth())
                    && event.getY() > location[1] && event.getY() < (location[1] + view.getHeight());
            return inRect;
        }
        return false;
    }

    /**
     * 改变软键盘状态
     *
     * @param view
     */
    public static void toggleSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, 0);
        }
    }
}
