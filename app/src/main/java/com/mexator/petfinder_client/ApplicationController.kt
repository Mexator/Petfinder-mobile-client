package com.mexator.petfinder_client

import androidx.multidex.MultiDexApplication
import com.mexator.petfinder_client.di.networkModule
import com.mexator.petfinder_client.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationController : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ApplicationController)
            modules(listOf(networkModule, storageModule))
        }
    }
}
