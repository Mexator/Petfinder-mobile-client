package com.mexator.petfinder_client.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.mvvm.viewmodel.StartViewModel
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

    private fun setupCheckAccountSubscription() {
        val job =
            viewModel.checkAccountExistence()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { value ->
                    val dest =
                        if (value) MainActivity::class.java
                        else LoginActivity::class.java

                    val intent = Intent(this, dest)

                    startActivity(intent)
                    this.finish()
                }
        compositeDisposable.add(job)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}