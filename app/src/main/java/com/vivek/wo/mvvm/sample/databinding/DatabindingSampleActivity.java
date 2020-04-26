package com.vivek.wo.mvvm.sample.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vivek.wo.mvvm.sample.R;

public class DatabindingSampleActivity extends AppCompatActivity {

    private SampleViewModel mSampleViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySampleDatabindingBinding databindingBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_sample_databinding);
        mSampleViewModel = new SampleViewModel("w", "123456");
        databindingBinding.setViewmodel(mSampleViewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
