package com.vivek.wo.mvvm.sample.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SampleViewModel extends ViewModel {
    private final LiveData<String> sampleLiveData = new MutableLiveData<>();


}
