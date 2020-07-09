package com.example.rxhomework.di

import android.accounts.AccountManager
import com.example.rxhomework.storage.StorageManager
import org.koin.dsl.module

val storageModule = module {
    single { StorageManager }

    single { AccountManager.get(get()) }
}