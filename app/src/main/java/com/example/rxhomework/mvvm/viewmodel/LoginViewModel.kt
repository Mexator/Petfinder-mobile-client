package com.example.rxhomework.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rxhomework.data.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val compositeDisposable = CompositeDisposable()

    fun checkUserCredentials(username: String, password: String, onFinish: (Boolean) -> Unit) {
        val job = repository.areUserCredentialsValid(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> onFinish(result) },
                { onFinish(false) })
        compositeDisposable.add(job)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}