package com.example.rxhomework.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavOptions
import com.example.rxhomework.R
import com.example.rxhomework.extensions.getTag
import com.example.rxhomework.mvvm.viewmodel.LoginViewModel
import com.example.rxhomework.network.api_interaction.CookieHolder
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        button_submit.setOnClickListener { onConfirmButtonClicked() }
    }

    private fun onConfirmButtonClicked() {
        viewModel.checkUserCredentials(
            login_edit.editText!!.text.toString(),
            password_edit.editText!!.text.toString(),
            ::onCheckFinished
        )
    }

    private fun onCheckFinished(checkResult: Boolean) {
        if (checkResult)
            onSuccessfulLogin()
        else
            Log.d(getTag(), "Wrong login")
    }

    private fun onSuccessfulLogin() {
        Toast.makeText(this, CookieHolder.userCookie, Toast.LENGTH_SHORT).show()
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainIntent)
    }
}