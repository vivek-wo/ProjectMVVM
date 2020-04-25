package com.vivek.wo.mvvm.sample.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

public class SampleViewModel extends BaseObservable {
    public final ObservableField<String> account = new ObservableField<>();
    private String password;

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
