package com.mexator.petfinder_client.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.Repository
import com.mexator.petfinder_client.storage.StorageManager
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class StartViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()
    private val storageManager: StorageManager by inject()

    fun checkAccountExistence(): Single<Boolean> {
        val loginNotEmpty = storageManager.loadCredentials().first.isNotEmpty()
        val passNotEmpty = storageManager.loadCredentials().second.isNotEmpty()
        return Single.just(loginNotEmpty and passNotEmpty)
    }

    fun checkAccountValidity(): Single<Boolean> {
        val (username, password) = storageManager.loadCredentials()
        return repository.areUserCredentialsValid(username, password)
            .onErrorReturnItem(false)
    }
}