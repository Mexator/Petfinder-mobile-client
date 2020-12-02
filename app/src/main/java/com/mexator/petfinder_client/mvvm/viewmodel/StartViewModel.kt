package com.mexator.petfinder_client.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.UserDataRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * [StartViewModel] is a ViewModel for the splash screen of application. Its View can
 * subscribe to [isReLoginNeeded] to know whether user should be redirected to
 * re-enter their credentials to obtain new access token
 */
class StartViewModel : ViewModel(), KoinComponent {
    private val repository: UserDataRepository by inject()

    /**
     * Return [Boolean] value wrapped in [Single] that should be used by View
     * to decide whether to navigate to Login screen or not
     */
    fun isReLoginNeeded(): Single<Boolean> {
        return repository
            .loadCookieFromDisk()
            .subscribeOn(Schedulers.io())
    }
}