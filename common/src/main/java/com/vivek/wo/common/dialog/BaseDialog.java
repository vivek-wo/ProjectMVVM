package com.vivek.wo.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

public class BaseDialog extends Dialog {
    private View contentView;

    public BaseDialog(@NonNull Context context) {
        this(context, 0);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = createContentView();
        if (contentView != null) {
            setContentView(contentView);
        }
    }

    public void cannotBack() {
        setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
            return false;
        });
    }

    public void setWindowWidth(int width) {
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = width;
        getWindow().setAttributes(p);
    }

    public void setWindowHeight(int height) {
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = height;
        getWindow().setAttributes(p);
    }

    public View getView() {
        return contentView;
    }

    protected View createContentView() {
        return null;
    }

    public void setWindowWidthPercent(float percent) {
        int[] size = getDisplayMetricsSize();
        setWindowWidth((int) (size[0] * percent));
    }

    public void setWindowHeightPercent(float percent) {
        int[] size = getDisplayMetricsSize();
        setWindowHeight((int) (size[1] * percent));
    }

    public void setWindowBackgroundDrawableResource(@DrawableRes int resId) {
        getWindow().setBackgroundDrawableResource(resId);
    }

    public void setWindowAnimations(@StyleRes int resId) {
        getWindow().setWindowAnimations(resId);
    }

    public void setWindowGravity(int gravity) {
        getWindow().setGravity(gravity);
    }

    protected int[] getDisplayMetricsSize() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
        return new int[]{metric.widthPixels, metric.heightPixels};
    }
}
