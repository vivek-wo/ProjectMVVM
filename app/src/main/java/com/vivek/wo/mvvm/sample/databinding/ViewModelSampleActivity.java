package com.vivek.wo.mvvm.sample.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vivek.wo.mvvm.sample.R;
import com.vivek.wo.mvvm.sample.viewmodel.SampleViewModel;

public class ViewModelSampleActivity extends AppCompatActivity {

    private SampleViewModel sampleViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_sample_databinding);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
