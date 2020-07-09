package com.example.rxhomework

import android.app.Application
import com.example.rxhomework.di.networkModule
import com.example.rxhomework.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationController : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ApplicationController)
            modules(listOf(networkModule, storageModule))
        }
    }
}
