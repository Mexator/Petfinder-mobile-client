package com.mexator.petfinder_client.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.data.remote.api_interaction.CookieHolder
import com.mexator.petfinder_client.storage.StorageManager
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class StartViewModel : ViewModel(), KoinComponent {
    private val repository: UserDataRepository by inject()
    private val storageManager: StorageManager by inject()

    fun checkAccountExistence(): Single<Boolean> {
        return Single.just(storageManager.loadCredentials())
            .doOnSuccess { CookieHolder.userCookie = it }
            .map { it.isNotEmpty() }
            .flatMap { value ->
                if (value) tryGetUser()
                else Single.just(false)
            }
    }

    private fun tryGetUser(): Single<Boolean> {
        return repository.getUser()
            .map { true }
            .onErrorReturn { false }
    }
}