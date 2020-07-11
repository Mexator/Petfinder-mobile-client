package com.mexator.petfinder_client.di

import android.accounts.AccountManager
import com.mexator.petfinder_client.storage.StorageManager
import org.koin.dsl.module

val storageModule = module {
    single { StorageManager }

    single { AccountManager.get(get()) }
}