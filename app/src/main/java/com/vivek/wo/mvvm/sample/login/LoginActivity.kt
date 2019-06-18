package com.vivek.wo.mvvm.sample.login

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.vivek.wo.mvvm.sample.R
import com.vivek.wo.mvvm.sample.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var activityLoginBinding: ActivityLoginBinding

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginViewModel = LoginViewModel()
        activityLoginBinding.viewmodel = loginViewModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("LoginActivity", "--> onDestroy")
    }
}