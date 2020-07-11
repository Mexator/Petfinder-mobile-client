package com.example.rxhomework.mvvm.viewmodel

import android.accounts.AccountManager
import androidx.lifecycle.ViewModel
import com.example.rxhomework.Accounts
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit

class StartViewModel : ViewModel(), KoinComponent {
    private val accountManager: AccountManager by inject()

    fun checkAccountExistence(): Single<Boolean> {
        return Single.just(accountManager.getAccountsByType(Accounts.ACCOUNT_TYPE).isNotEmpty())
            .delay(1000,TimeUnit.MILLISECONDS)
    }
}