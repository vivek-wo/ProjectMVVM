package com.vivek.wo.mvvm.sample.login

import android.databinding.ObservableField
import android.util.Log

class LoginViewModel {

    var account = ObservableField<String>()

    var password = ObservableField<String>()

    var inputMessage = ObservableField<String>()

    fun login() {
        inputMessage.set("${account.get()} , ${password.get()}")
        Log.w("LoginViewModel", "-->> ${account.get()} , ${password.get()}")
    }
}