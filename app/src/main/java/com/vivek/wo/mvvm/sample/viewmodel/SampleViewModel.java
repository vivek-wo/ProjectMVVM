package com.vivek.wo.mvvm.sample.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class SampleViewModel extends ViewModel {
    public final MutableLiveData<String> account = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();

    public SampleViewModel() {
    }

    public SampleViewModel(String account, String password) {
        this.account.setValue(account);
        this.password.setValue(password);
    }

    public void onActionLogin() {
        Log.w(getClass().getSimpleName(),
                "account: " + account.getValue() + ", password: " + password.getValue());
        this.password.setValue("168");
    }
}
