package com.vivek.wo.common.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

public class ProgressDialog extends BaseDialog {

    public ProgressDialog(@NonNull Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
        setWindowBackgroundDrawableResource(android.R.color.transparent);
    }

    public ProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected View createContentView() {
        return createDefaultProgressContentView();
    }

    private View createDefaultProgressContentView() {
        ProgressBar progressBar = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleInverse);
        return progressBar;
    }

}
