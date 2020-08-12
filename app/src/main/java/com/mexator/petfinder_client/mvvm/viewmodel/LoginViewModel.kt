package com.mexator.petfinder_client.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.UserDataRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginViewModel : ViewModel(), KoinComponent {
    private val repository: UserDataRepository by inject()

    private val progress: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun isCredentialDataValid(username: String, password: String): Single<Boolean> {
        return repository.login(username, password)
            .doOnSubscribe { progress.onNext(true) }
            .doOnEvent { _, _ -> progress.onNext(false) }
            .onErrorReturnItem(false)
    }

    fun getProgressIndicator(): Observable<Boolean> = progress
}