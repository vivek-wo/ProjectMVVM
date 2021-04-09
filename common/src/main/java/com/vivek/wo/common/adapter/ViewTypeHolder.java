package com.vivek.wo.common.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class ViewTypeHolder extends RecyclerView.ViewHolder {

    public ViewTypeHolder(@NonNull View itemView) {
        super(itemView);
    }

    public static class DataBinding<V extends ViewDataBinding> extends ViewTypeHolder {
        private V binding;

        public DataBinding(@NonNull View itemView) {
            super(itemView);
            try {
                binding = DataBindingUtil.bind(itemView);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        public V getBinding() {
            return binding;
        }
    }
}
