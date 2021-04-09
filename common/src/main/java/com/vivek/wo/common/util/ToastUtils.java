package com.vivek.wo.common.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtils {
    private static final int D_PADDING = 40;

    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static com.ut.common.util.ToastUtils defaultToastUtils;
    private static Context applicationContext;
    private static Toast sToast;

    private View view;

    protected int getOffsetX() {
        return 0;
    }

    protected int getOffsetY() {
        return 0;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    protected int getDuration() {
        return Toast.LENGTH_SHORT;
    }

    protected int getViewBackgroundDrawableResId() {
        return 0;
    }

    protected View createDefaultToastView() {
        TextView textView = new TextView(applicationContext);
        if (getViewBackgroundDrawableResId() != 0) {
            textView.setBackgroundResource(getViewBackgroundDrawableResId());
        } else {
            textView.setBackground(createDefaultShapeDrawable());
        }
        textView.setPadding(D_PADDING, D_PADDING, D_PADDING, D_PADDING);
        textView.setId(android.R.id.message);
        textView.setTextColor(Color.WHITE);
        return textView;
    }

    public View getToastUsedView() {
        if (view == null) {
            view = createDefaultToastView();
        }
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    public static void attachContext(Context context) {
        attachContext(context, new com.ut.common.util.ToastUtils());
    }

    public static void attachContext(Context context, com.ut.common.util.ToastUtils toastUtils) {
        applicationContext = context;
        defaultToastUtils = toastUtils;
    }

    public static void show(String message) {
        mainHandler.post(() -> {
            cancel();
            sToast = new Toast(applicationContext);
            sToast.setGravity(defaultToastUtils.getGravity(), defaultToastUtils.getOffsetX(), defaultToastUtils.getOffsetY());
            View view = defaultToastUtils.getToastUsedView();
            ((TextView) view.findViewById(android.R.id.message)).setText(message);
            sToast.setView(view);
            sToast.setDuration(defaultToastUtils.getDuration());
            sToast.show();
        });
    }

    public static void showAtCenter(String message) {
        mainHandler.post(() -> {
            cancel();
            sToast = new Toast(applicationContext);
            sToast.setGravity(Gravity.CENTER, defaultToastUtils.getOffsetX(), defaultToastUtils.getOffsetY());
            View view = defaultToastUtils.getToastUsedView();
            ((TextView) view.findViewById(android.R.id.message)).setText(message);
            sToast.setView(view);
            sToast.setDuration(defaultToastUtils.getDuration());
            sToast.show();
        });
    }

    public static void showAtBottom(String message) {
        mainHandler.post(() -> {
            cancel();
            sToast = new Toast(applicationContext);
            sToast.setGravity(Gravity.BOTTOM, defaultToastUtils.getOffsetX(), defaultToastUtils.getOffsetY());
            View view = defaultToastUtils.getToastUsedView();
            ((TextView) view.findViewById(android.R.id.message)).setText(message);
            sToast.setView(view);
            sToast.setDuration(defaultToastUtils.getDuration());
            sToast.show();
        });
    }

    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

    protected ShapeDrawable createDefaultShapeDrawable() {
        float[] outii = {D_PADDING, D_PADDING, D_PADDING, D_PADDING, D_PADDING, D_PADDING, D_PADDING, D_PADDING};
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(outii, null, null));
        shapeDrawable.getPaint().setColor(Color.BLACK);
        return shapeDrawable;
    }
}
