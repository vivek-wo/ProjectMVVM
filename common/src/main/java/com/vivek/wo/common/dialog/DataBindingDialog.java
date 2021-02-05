package com.vivek.wo.common.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class DataBindingDialog<T extends ViewDataBinding> extends BaseDialog {
    protected T binding;

    public DataBindingDialog(@NonNull Context context) {
        super(context);
    }

    public DataBindingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected View createContentView() {
        int layoutId = initContentViewLayoutId();
        if (layoutId == 0) {
            return null;
        }
        binding = DataBindingUtil.inflate(getLayoutInflater(), layoutId, null, false);
        return binding.getRoot();
    }

    protected int initContentViewLayoutId() {
        return 0;
    }

    public T getBinding() {
        return binding;
    }
}
