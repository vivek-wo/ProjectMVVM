package com.vivek.wo.mvvm.sample.viewmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ViewModelSampleActivity extends AppCompatActivity {

    private SampleViewModel sampleViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
