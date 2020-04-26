package com.vivek.wo.mvvm.sample.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.util.Log;

import com.vivek.wo.mvvm.sample.BR;

public class SampleViewModel extends BaseObservable {
    public final ObservableField<String> account = new ObservableField<>();
    private String password;

    public SampleViewModel(String account, String password) {
        this.account.set(account);
        this.password = password;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void onActionLogin() {
        Log.w(getClass().getSimpleName(),
                "account: " + account.get() + ", password: " + password);
        this.password = "168";
        notifyPropertyChanged(BR.password);
    }
}
