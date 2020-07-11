package com.example.rxhomework.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rxhomework.data.Repository
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    fun isCredentialDataValid(username: String, password: String):Single<Boolean> {
        return repository.areUserCredentialsValid(username, password)
            .onErrorReturnItem(false)
    }
}