package com.vivek.wo.mvvm.sample.viewmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vivek.wo.mvvm.sample.R;
import com.vivek.wo.mvvm.sample.databinding.ActivitySampleViewmodelBinding;

public class ViewModelSampleActivity extends AppCompatActivity {

    private SampleViewModel mSampleViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySampleViewmodelBinding databinding = DataBindingUtil
                .setContentView(this, R.layout.activity_sample_viewmodel);
        mSampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        databinding.setViewmodel(mSampleViewModel);
        databinding.setLifecycleOwner(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
