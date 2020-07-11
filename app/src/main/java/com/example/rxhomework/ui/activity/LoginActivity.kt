package com.example.rxhomework.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rxhomework.R
import com.example.rxhomework.mvvm.viewmodel.LoginViewModel
import com.example.rxhomework.network.api_interaction.CookieHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setupProgressSubscription()
        button_submit.setOnClickListener { onConfirmButtonClicked() }
        button_sign_up.setOnClickListener { onSignUpButtonClicked() }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun onConfirmButtonClicked() {
        val login = login_edit.editText!!.text.toString()
        val pass = password_edit.editText!!.text.toString()
        val job = viewModel.isCredentialDataValid(login, pass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { value -> onCheckFinished(value) }
        compositeDisposable.add(job)
    }

    private fun onCheckFinished(checkResult: Boolean) {
        if (checkResult)
            onSuccessfulLogin()
        else
            Toast.makeText(this, R.string.text_error_login, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccessfulLogin() {
        Toast.makeText(this, CookieHolder.userCookie, Toast.LENGTH_SHORT).show()
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainIntent)
        finish()
    }

    private fun onSignUpButtonClicked() {
        val signUpUrl = "https://www.petfinder.com/"
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(signUpUrl)
        startActivity(browserIntent)
    }

    private fun setupProgressSubscription() {
        val job =
            viewModel.getProgressIndicator()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    progress.visibility = if (it) View.VISIBLE else View.GONE
                    button_submit.isEnabled = !it
                }
        compositeDisposable.add(job)
    }
}