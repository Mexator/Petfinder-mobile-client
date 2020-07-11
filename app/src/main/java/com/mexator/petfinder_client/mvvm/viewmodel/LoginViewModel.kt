package com.mexator.petfinder_client.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.Repository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val progress: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun isCredentialDataValid(username: String, password: String): Single<Boolean> {
        return repository.areUserCredentialsValid(username, password)
            .doOnEvent { _, _ -> progress.onNext(false) }
            .doOnSubscribe { progress.onNext(true) }
            .onErrorReturnItem(false)
    }

    fun getProgressIndicator(): Observable<Boolean> = progress
}