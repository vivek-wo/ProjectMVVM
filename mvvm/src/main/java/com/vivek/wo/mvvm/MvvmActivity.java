package com.vivek.wo.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class MvvmActivity<V extends ViewDataBinding, VM extends MvvmViewModel> extends AppCompatActivity {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams(savedInstanceState);
        initViewDataBinding();
        initViewObservable();
        initData();
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    protected abstract int initContentViewLayoutId();

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    protected abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    protected VM initViewModel() {
        return null;
    }

    private void initViewDataBinding() {
        binding = DataBindingUtil.setContentView(this, initContentViewLayoutId());
        viewModelId = initVariableId();
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = MvvmViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);
        }
        if (viewModelId == 0) {
            return;
        }
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
    }

    /**
     * 初始化参数数据，包括页面间数据传递
     *
     * @param savedInstanceState
     */
    public void initParams(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 初始化数据加载请求
     */
    public void initData() {
    }

    /**
     * 初始化监听器
     */
    public void initViewObservable() {
    }

    protected <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return new ViewModelProvider(activity).get(cls);
    }
}
