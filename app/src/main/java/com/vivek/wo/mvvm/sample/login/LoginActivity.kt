package com.vivek.wo.mvvm.sample.login

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vivek.wo.mvvm.sample.R
import com.vivek.wo.mvvm.sample.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var activityLoginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    }
}