package com.vivek.wo.mvvm.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vivek.wo.common.dialog.BottomSheetDialog;

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

    public void onProgressDialog(View view) {
        BottomSheetDialog dialog = new BottomSheetDialog(this) {
            @Override
            protected View createContentView() {
                return LayoutInflater.from(getContext()).inflate(R.layout.dialog_content_tips, null, false);
            }
        };
        dialog.show();
    }

}
