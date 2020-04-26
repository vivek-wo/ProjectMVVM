package com.vivek.wo.mvvm.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vivek.wo.mvvm.sample.databinding.DatabindingSampleActivity;
import com.vivek.wo.mvvm.sample.viewmodel.ViewModelSampleActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onDatabindingClick(View v) {
        Intent intent = new Intent(this, DatabindingSampleActivity.class);
        startActivity(intent);
    }

    public void onViewModelClick(View v) {
        Intent intent = new Intent(this, ViewModelSampleActivity.class);
        startActivity(intent);
    }
}
