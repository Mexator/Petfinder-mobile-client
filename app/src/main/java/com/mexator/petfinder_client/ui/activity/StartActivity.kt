package com.mexator.petfinder_client.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.mvvm.viewmodel.StartViewModel
import com.mexator.petfinder_client.network.api_interaction.CookieHolder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_start.*
import org.koin.core.KoinComponent

class StartActivity : AppCompatActivity(), KoinComponent {
    private lateinit var viewModel: StartViewModel

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val anim = AnimationUtils.loadAnimation(this, R.anim.animation_scale_loop)
        splash.startAnimation(anim)

        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)

        setupCheckAccountSubscription()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun setupCheckAccountSubscription() {
        val job =
            viewModel.checkAccountExistence()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { if (it) viewModel.checkAccountValidity() else Single.just(it) }
                .subscribe { value ->
                    onAccountCheckFinished(value)
                }
        compositeDisposable.add(job)
    }

    private fun onAccountCheckFinished(result: Boolean) {
        val dest =
            if (result) MainActivity::class.java
            else LoginActivity::class.java

        val intent = Intent(this, dest)

        startActivity(intent)
        finish()
    }
}